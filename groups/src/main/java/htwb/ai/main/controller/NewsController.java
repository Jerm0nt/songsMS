package htwb.ai.main.controller;

import htwb.ai.main.model.Group;
import htwb.ai.main.model.News;
import htwb.ai.main.services.AuthRestWrapper;
import htwb.ai.main.services.IGroupService;
import htwb.ai.main.services.INewsService;
import htwb.ai.main.services.SongListRestWrapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value="/group/news")
public class NewsController {

  @Autowired
  INewsService newsService;

  @Autowired
  IGroupService groupService;

  @Autowired
  AuthRestWrapper authRestWrapper;

  @Autowired
  SongListRestWrapper songListWrapper;

  @PostMapping(consumes="application/json")
  public ResponseEntity postNews(@RequestBody News news,
                                 @RequestHeader(name = "Authorization", required = false) String token){
    if(token==null||!authRestWrapper.isTokenValid(token)){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    try{
      String userId = authRestWrapper.getUserIdByToken(token);
      if(news.getGroupId()==null){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }
      if(!groupService.isUserMember(userId,news.getGroupId())){
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
      }
      if(news.getSongListId()!=0){
        if(!songListWrapper.getOwner(news.getSongListId()).equals(userId)
          || songListWrapper.isSongListPrivate(news.getSongListId())){
          return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
      }
      int newsId = newsService.postNews(userId, news);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Location", "http://localhost:8080/songs/playist/"+String.valueOf(newsId));
      return new ResponseEntity(headers,HttpStatus.OK);
    }catch(NotFoundException e){
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
  @GetMapping(produces = "application/json")
  public ResponseEntity getAllNews(@RequestHeader(name = "Authorization", required = false) String token){
    if(token==null||!authRestWrapper.isTokenValid(token)){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    try{
      String userId = authRestWrapper.getUserIdByToken(token);
      ArrayList<Group> groupList = groupService.getAllGroups(userId);
      ArrayList<News> newsList = newsService.getAllNews(groupList);
      return new ResponseEntity(newsList, HttpStatus.OK);
    }catch(NotFoundException e){
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
  @GetMapping(produces = "application/json", value="/byGroup")
  public ResponseEntity getNewsByGroup(@RequestHeader(name="Group", required = false) String groupId,
                                       @RequestHeader(name = "Authorization", required = false) String token){
    if(token==null||!authRestWrapper.isTokenValid(token)){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    if(groupId==null){
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    try{
      String userId = authRestWrapper.getUserIdByToken(token);
      if(groupService.isUserMember(userId,groupId)){
        ArrayList<News> newsList = newsService.getNewsByGroupId(groupId);
        return new ResponseEntity(newsList,HttpStatus.OK);
      }else{
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
      }

    } catch (NotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }

}
