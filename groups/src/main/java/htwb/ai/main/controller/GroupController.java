package htwb.ai.main.controller;

import htwb.ai.main.model.Group;
import htwb.ai.main.services.AuthRestWrapper;
import htwb.ai.main.services.IGroupService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;

@RestController
@RequestMapping(value="/group")
public class GroupController {

  @Autowired
  IGroupService groupService;

  @Autowired
  AuthRestWrapper authRestWrapper;

  @GetMapping(produces = {"application/json", "application/xml"})
  public ResponseEntity<ArrayList<Group>> getAllGroups(
    @RequestHeader(name = "Authorization", required = false) String token){
    if(token==null){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    if(!authRestWrapper.isTokenValid(token)){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    ArrayList groupList;
    try {
      groupList = groupService.findAll();
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(groupList, HttpStatus.OK);
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<Group> postGroup(@RequestBody Group group,
                                         @RequestHeader(name="Authorization", required = false) String token){
    if(!authRestWrapper.isTokenValid(token)|| token==null){
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    try{
      String userId = authRestWrapper.getUserIdByToken(token);
      groupService.postGroup(group, userId);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Location", "http://localhost:8080/group?groupId="+group.getGroupId());
      return new ResponseEntity<>(group, headers, HttpStatus.CREATED);
    }catch(Exception e){
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }
  @PutMapping(value="/add", consumes = "application/json")
  public ResponseEntity addMember(@RequestBody Group group,
                                  @RequestHeader(name = "Authorization", required = false) String token) {
    try{
      if (!authRestWrapper.isTokenValid(token)
        || token == null
        || !authRestWrapper.getUserIdByToken(token).equals(groupService.getGroupById(group.getGroupId()).getOwner())) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      if(group.getMemberList().size()!=1||group.getMemberList()==null){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }
      groupService.addMember(group.getGroupId(), group.getMemberList().get(0));
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    }catch(NotFoundException e){
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }catch(InvalidParameterException e){
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }
  @DeleteMapping(value="/leave", consumes="text/plain")
  public ResponseEntity leaveGroup(@RequestBody String groupId,
                                   @RequestHeader(name = "Authorization", required = false) String token) {
    try {
      if (!authRestWrapper.isTokenValid(token)
        || token == null) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      groupService.leaveGroup(groupId, authRestWrapper.getUserIdByToken(token));
    } catch (NotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } catch (InvalidParameterException e){
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
