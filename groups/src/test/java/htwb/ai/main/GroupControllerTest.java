package htwb.ai.main;

import htwb.ai.main.controller.GroupController;
import htwb.ai.main.model.Genre;
import htwb.ai.main.model.Group;
import htwb.ai.main.services.AuthRestWrapper;
import htwb.ai.main.services.GroupService;
import htwb.ai.main.services.IGroupService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class GroupControllerTest {
  //test-object
  GroupController groupController;
  //mockObjekte
  GroupService mockGroupService;
  AuthRestWrapper mockAuthRestWrapper;
  //token
  String tokenValidOwner;
  String tokenValidMember;
  String tokenValidBad;
  String tokenInvalid;
  //Groups
  Group group1;
  Group group2;
  //Group-List
  ArrayList<Group> groupList;
  //userIds
  String userIdOwner;
  String userIdMember;
  String userIdBad;
  //group-attributes
  String groupId1;
  String groupId2;
  String description;
  Genre genre;
  ArrayList<String> memberList;

  @BeforeEach public void setUp() throws NotFoundException {
    groupController = new GroupController();
    mockAuthRestWrapper = Mockito.mock(AuthRestWrapper.class);
    mockGroupService = Mockito.mock(GroupService.class);
    groupController.setServices(mockGroupService,mockAuthRestWrapper);
    tokenValidOwner="valid";
    tokenValidBad="validBad";
    tokenValidMember="validMember";
    tokenInvalid="invalid";
    group1 = new Group();
    group2 = new Group();
    groupList = new ArrayList<>();
    groupId1 = "1";
    groupId2 = "2";
    group1.setGroupId(groupId1);
    group2.setGroupId(groupId2);
    userIdOwner = "owner";
    userIdMember = "member";
    userIdBad = "badUser";
    description = "description";
    genre = Genre.ROCK;
    memberList = new ArrayList<>();
    group1.setMemberList(memberList);

    when(mockAuthRestWrapper.getUserIdByToken(tokenValidOwner)).thenReturn(userIdOwner);
    when(mockAuthRestWrapper.getUserIdByToken(tokenValidMember)).thenReturn(userIdMember);
    when(mockAuthRestWrapper.getUserIdByToken(tokenValidBad)).thenReturn(userIdBad);
    when(mockAuthRestWrapper.getUserIdByToken(tokenInvalid)).thenThrow(NotFoundException.class);

    when(mockAuthRestWrapper.isTokenValid(tokenValidOwner)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenValidMember)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenValidBad)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenInvalid)).thenReturn(false);

    when(mockGroupService.getGroupById(groupId1)).thenReturn(group1);
    when(mockGroupService.getGroupById(groupId2)).thenReturn(group2);
    groupList.add(group1);
    when(mockGroupService.findAll()).thenReturn(groupList);
  }
  @Test public void getAllGroupsTest1Good(){
    assertTrue(groupController.getAllGroups(tokenValidMember).getBody().contains(group1));
    assertTrue(groupController.getAllGroups(tokenValidMember).getStatusCode().equals(HttpStatus.OK));
  }
  @Test public void getAllGroupsTest2NoGroups() throws NotFoundException {
    when(mockGroupService.findAll()).thenThrow(EntityNotFoundException.class);
    assertTrue(groupController.getAllGroups(tokenValidMember).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  @Test public void getAllGroupsTest3WrongToken(){
    assertTrue(groupController.getAllGroups(tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void postGroupTest1Good(){
    groupController.postGroup(group1, tokenValidOwner);
    Mockito.verify(mockGroupService).postGroup(group1, userIdOwner);
  }
  @Test public void postGroupTest2Unauthorized(){
    assertTrue(groupController.postGroup(group1, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void postGroupTest3BadRequest(){
    doThrow(InvalidParameterException.class).when(mockGroupService).postGroup(group2, userIdOwner);
    assertTrue(groupController.postGroup(group2,tokenValidOwner).getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }
  @Test public void addMemberTest1Good(){
    group1.setOwner(userIdOwner);
    group1.getMemberList().add(userIdMember);
    assertTrue(groupController.addMember(group1, tokenValidOwner).getStatusCode().equals(HttpStatus.NO_CONTENT));

  }
  @Test public void addMemberTest2Unauthorized(){
    group1.setOwner(userIdOwner);
    group1.getMemberList().add(userIdMember);
    assertTrue(groupController.addMember(group1, tokenValidMember).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void addMemberTest3Invalid(){
    doThrow(InvalidParameterException.class).when(mockGroupService).addMember(groupId1,userIdOwner);
    group1.getMemberList().add(userIdOwner);
    group1.getMemberList();
    group1.setOwner(userIdOwner);
    assertTrue(groupController.addMember(group1, tokenValidOwner).getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }
  //leaveGroup-test
  @Test public void leaveGroupTest1Good(){
    assertTrue(groupController.leaveGroup(groupId1,tokenValidOwner).getStatusCode().equals(HttpStatus.NO_CONTENT));
  }
  @Test public void leaveGroupTest2Owner() throws NotFoundException {
    doThrow(InvalidParameterException.class).when(mockGroupService).leaveGroup(groupId1,userIdOwner);
    assertTrue(groupController.leaveGroup(groupId1,tokenValidOwner).getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }
  @Test public void leaveGroupTest3Unauthorized(){
    assertTrue(groupController.leaveGroup(groupId1, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
}
