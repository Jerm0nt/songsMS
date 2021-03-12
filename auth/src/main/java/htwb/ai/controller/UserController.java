package htwb.ai.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import htwb.ai.model.User;
import htwb.ai.services.IUserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
