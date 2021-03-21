package htwb.ai.main.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import htwb.ai.main.model.User;
import htwb.ai.main.services.IUserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value="/auth")
public class UserController {

  @Autowired
  IUserService userService;

  @PostMapping(produces = "text/plain", consumes ="application/json")
  public ResponseEntity<String> authorize(@RequestBody String jsonBody)
    throws IOException {
    try{
      Gson gson = new GsonBuilder().serializeNulls().create();
      User reqUser = gson.fromJson(jsonBody, User.class);
      String password = reqUser.getPassword();
      User user = userService.getUserByUserId(reqUser.getUserId());
      if (user.getPassword().equals(password)) {
        UUID uuid = UUID.randomUUID();
        userService.setToken(user, uuid.toString());
        return new ResponseEntity<String> (uuid.toString(), HttpStatus.OK);
      }else{
        return new ResponseEntity<String> ("Declined!!", HttpStatus.UNAUTHORIZED);
      }
    }catch(NotFoundException e){
      return new ResponseEntity<String>("Declined: No such user!",
        HttpStatus.UNAUTHORIZED);
    }catch(JsonSyntaxException e){
      return new ResponseEntity<String>("Bad Json Format!", HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value="/valid/{token}")
  public ResponseEntity<Boolean> isTokenValid(@PathVariable(value="token") String token){
    try {
      userService.getUserByToken(token);
      return ResponseEntity.ok(true);
    } catch (NotFoundException e) {
      e.printStackTrace();
      return ResponseEntity.ok(false);
    }
  }

  @GetMapping(value="/match")
  public ResponseEntity<Boolean> doTokenAndIdMatch(@RequestParam(value="userId") String userId,
                                  @RequestParam(value="token") String token){
    try {
      userService.getUserByUserId(userId);
      if(userService.getUserByToken(token).getUserId().equals(userId)){
        return ResponseEntity.ok(true);
      }else{
        return ResponseEntity.ok(false);
      }
    } catch (NotFoundException e) {
      e.printStackTrace();
      return ResponseEntity.ok(false);
    }
  }

  @GetMapping(value="/validUser/{userId}")
  public ResponseEntity<Boolean> isUserValid(@PathVariable(value="userId") String userId){
    try {
      userService.getUserByUserId(userId);
      return ResponseEntity.ok(true);
    } catch (NotFoundException e) {
      e.printStackTrace();
      return ResponseEntity.ok(false);
    }
  }

  @GetMapping(value="/getId/{token}")
  public String getUserIdByToken(@PathVariable(value="token") String token){
    try{
      return userService.getUserByToken(token).getUserId();
    }catch (NotFoundException e){
      return "User not found!";
    }
  }
}
