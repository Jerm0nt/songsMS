package htwb.ai.main.controller;

import htwb.ai.main.model.Group;
import htwb.ai.main.services.AuthRestWrapper;
import htwb.ai.main.services.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value="/group")
public class GroupController {

  @Autowired
  IGroupService groupService;

  @Autowired
  private AuthRestWrapper authRestWrapper;

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
}
