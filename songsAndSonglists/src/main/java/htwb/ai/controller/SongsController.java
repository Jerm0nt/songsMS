package htwb.ai.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import htwb.ai.model.Songs;
import htwb.ai.services.ISongsService;
import htwb.ai.services.IUserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;

@RestController
@RequestMapping(value="/songs")
public class SongsController {

  @Autowired
  private ISongsService songsService;

  @Autowired
  private IUserService userService;

    @GetMapping(value="/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Songs> getSong(@PathVariable(value="id") Integer id,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
      if(!userService.isTokenValid(token) || token==null){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      Songs song;
      try{
        song = songsService.getSong(id);
      }
      catch(Exception e){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    public ResponseEntity<ArrayList<Songs>> allSongs(
      @RequestHeader(name = "Authorization", required = false) String token) {
      if(token==null){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      if(!userService.isTokenValid(token)){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      ArrayList songsList;
      try {
        songsList = songsService.findAll();
      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(songsList, HttpStatus.OK);
    }

  @PostMapping(consumes = "application/json")
  public ResponseEntity postSong(@RequestBody String jsonBody,
                                 @RequestHeader(name = "Authorization", required = false) String token) {
    if(!userService.isTokenValid(token)|| token==null){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Gson gson = new GsonBuilder().serializeNulls().create();
    try {
      Songs song = gson.fromJson(jsonBody, Songs.class);
      if(song.getTitle()==null){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }
      int id = songsService.postSong(song);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Location", "http://localhost:8080/songsWS-TeamRobertJerome/rest/songs/"+String.valueOf(id));
      return new ResponseEntity(headers, HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity((HttpStatus.BAD_REQUEST));
    }
  }

    @PutMapping(value="/{id}", consumes ="application/json")
    public ResponseEntity putSong(@RequestBody String jsonBody, @PathVariable (value = "id") Integer id,
                                  @RequestHeader(name = "Authorization", required = false) String token) {
      if(!userService.isTokenValid(token)|| token==null){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      try {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Songs song = gson.fromJson(jsonBody, Songs.class);
        songsService.putSong(id,song);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
      } catch (NotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.NOT_FOUND);
      } catch (InvalidParameterException e) {
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }catch (JsonSyntaxException e){
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity deleteSong(@PathVariable (value="id") Integer id,
                                     @RequestHeader(name = "Authorization", required = false) String token) {

      if(!userService.isTokenValid(token)|| token==null){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      try {
        songsService.deleteSong(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
      } catch (NotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.NOT_FOUND);
      }
    }

  public void setServices(ISongsService mockSongsServie, IUserService mockUserService) {
      this.userService = mockUserService;
      this.songsService = mockSongsServie;
  }
}

