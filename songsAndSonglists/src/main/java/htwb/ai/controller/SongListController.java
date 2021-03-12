package htwb.ai.controller;


import htwb.ai.model.SongList;
import htwb.ai.model.User;
import htwb.ai.services.ISongListService;
import htwb.ai.services.IUserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping(value="/songLists")
public class SongListController {
  @Autowired
  ISongListService songListService;

  @Autowired
  IUserService userService;

  @GetMapping(value="/{id}", produces = {"application/json", "application/xml"})
  public ResponseEntity getSongListById(@PathVariable(value="id") Integer id,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
    if(!userService.isTokenValid(token) || token==null){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    SongList songList;
    try{
      songList = songListService.getSongList(id);
    }catch (Exception e){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    try{
      //wenn SongList privat, muss token zu SongList-Owner gehören
      User u = songList.getUser();
      if(songList.isPrivate() &&
        !songList.getUser().getUserId().equals(userService.getUserByToken(token).getUserId())){
        return new ResponseEntity(HttpStatus.FORBIDDEN);
      }
    }catch(Exception e){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(songList, HttpStatus.OK);
  }
  @PostMapping(consumes = "application/json")
  public ResponseEntity<SongList> postSongList(@RequestBody SongList songList,
                                     @RequestHeader(name="Authorization", required = false) String token){
    if(!userService.isTokenValid(token)|| token==null){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    try{
      int id = songListService.postSongList(songList, token);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Location", "http://localhost:8080/songsWS-TeamRobertJerome/rest/songLists/"+String.valueOf(id));
      return new ResponseEntity<>(songList,headers, HttpStatus.CREATED);
    }catch (Exception e){
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping(produces = {"application/json", "application/xml"})
  public ResponseEntity getSongListByUser(@RequestParam(value="userId") String userId,
                           @RequestHeader(name = "Authorization", required = false) String token) {
    if(!userService.isTokenValid(token)|| token==null){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    try{
      Set<SongList> songListSet = userService.getSongListSet(userId, token);
      return new ResponseEntity(songListSet, HttpStatus.OK);
    }catch(Exception e){
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
  @DeleteMapping(value="/{id}")
  public ResponseEntity deleteSongList(@PathVariable(value="id") Integer id,
                                       @RequestHeader(name = "Authorization", required = false) String token) {
    if (!userService.isTokenValid(token) || token == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    try {
      if (songListService.deleteSongList(id, token)) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
      } else {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
      }
    } catch (NotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
