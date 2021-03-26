package htwb.ai.main;

import htwb.ai.main.model.Group;
import htwb.ai.main.repository.GroupRepository;
import htwb.ai.main.services.GroupService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class GroupServiceTest {
  //test-object
  GroupService groupService;
  //mock-object
  GroupRepository mockGroupRepository;
  //groups
  Group group1;
  Group newGroup;
  //group-list
  ArrayList<Group> groupArrayList;
  //userId
  String userIdGood;
  String userIdBad;
  //groupId
  String groupIdGood;
  String groupIdBad;


  @BeforeEach public void setUp(){
    groupService = new GroupService();
    mockGroupRepository = Mockito.mock(GroupRepository.class);
    groupService.setRepository(mockGroupRepository);

    group1 = new Group();
    newGroup = new Group();
    groupArrayList = new ArrayList<>();
    groupArrayList.add(group1);
    userIdBad = "bad";
    userIdGood = "good";
    groupIdGood = "goodGroup";
    groupIdBad = "badGroup";

    when(mockGroupRepository.findAll()).thenReturn(groupArrayList);
    when(mockGroupRepository.findById(groupIdGood)).thenReturn(Optional.of(group1));
    when(mockGroupRepository.findById(groupIdBad)).thenThrow(EntityNotFoundException.class);

  }

  @Test public void findAllTest1Good() throws NotFoundException {
    assertTrue(groupService.findAll().contains(group1));
  }
  @Test public void findAllTest2NotFound(){
    when(mockGroupRepository.findAll()).thenThrow(EntityNotFoundException.class);
    try{
      groupService.findAll().contains(group1);
    }catch (NotFoundException e){
      assertTrue(e.getMessage().equals("Keine Groups"));
    }
  }
  @Test public void postGroupTest1Good(){
    group1.setGroupId(groupIdGood);
    newGroup.setGroupId("newGroup");
    groupService.postGroup(newGroup, userIdGood);
    assertTrue(newGroup.getOwner().equals(userIdGood));
    assertTrue(newGroup.getMemberList().contains(userIdGood));
    assertTrue(newGroup.getFoundingDate()!=null);
  }
  @Test public void postGroupTest2Member(){
    try{
      newGroup.setMemberList(new ArrayList<String>());
      group1.setGroupId(groupIdGood);
      newGroup.setGroupId("newGroup");
      groupService.postGroup(newGroup, userIdGood);

    }catch (InvalidParameterException e){
      assertTrue(e.getMessage().equals("Memberliste muss leer sein!"));
    }
  }
  @Test public void postGroupTest3groupIdExists(){
    group1.setGroupId(groupIdGood);
    newGroup.setGroupId(groupIdGood);
    try{
      groupService.postGroup(newGroup, userIdGood);
    }catch (InvalidParameterException e){
      assertTrue(e.getMessage().equals("GroupId existiert bereits"));
    }
  }
  //getGroupById
  @Test public void getGroupByIdTest1Good() throws NotFoundException {
    group1.setGroupId(groupIdGood);
    assertTrue(groupService.getGroupById(groupIdGood).getGroupId().equals(groupIdGood));
  }
  @Test public void getGroupByIdTest2NotFound(){
    try{
      groupService.getGroupById(groupIdBad);
    }catch (Exception e){
      assertTrue(e.getMessage().equals("Keine Gruppe mit dieser ID!"));
    }
  }
  //addMember
  @Test public void addMemberTest1Good(){
    group1.setMemberList(new ArrayList<String>());
    groupService.addMember(groupIdGood,userIdGood);
    assertTrue(group1.getMemberList().contains(userIdGood));
  }
  @Test public void addMemberTest2AlreadyInGroup(){
    group1.setMemberList(new ArrayList<String>());
    group1.getMemberList().add(userIdGood);
    try{
      groupService.addMember(groupIdGood,userIdGood);
    }catch (Exception e){
      assertTrue(e.getMessage().equals("Nutzer schon in Gruppe!"));
    }
  }
  //leaveGroup
  @Test public void leaveGroupTest1Good() throws NotFoundException {
    group1.setMemberList(new ArrayList<>());
    group1.getMemberList().add("neuerUser");
    group1.setOwner(groupIdGood);
    groupService.leaveGroup(groupIdGood,"neuerUser");
  }
  @Test public void leaveGroupTest2NotFound() throws NotFoundException {
    group1.setMemberList(new ArrayList<>());
    group1.getMemberList().add("neuerUser");
    group1.setOwner(userIdGood);
    try{
      groupService.leaveGroup(groupIdBad,"neuerUser");
    }catch(Exception e){
      assertTrue(e.getMessage().equals("Gruppe nicht gefunden!"));
    }
  }
  @Test public void leaveGroupTest3Owner(){
    group1.setOwner(userIdGood);
    try{
      groupService.leaveGroup(groupIdGood,userIdGood);
    }catch(Exception e){
      assertTrue(e.getMessage().equals("Owner kann nicht aus Gruppe austreten!"));
    }
  }
  @Test public void leaveGroupTest4NotInGroup(){
    group1.setMemberList(new ArrayList<>());
    group1.getMemberList().add("neuerUser");
    group1.setOwner(userIdGood);
    try{
      groupService.leaveGroup(groupIdGood,"andererUser");
    }catch(Exception e){
      assertTrue(e.getMessage().equals("User nicht in dieser Gruppe!"));
    }
  }
  //isUserMember-Test
  @Test public void isUserMemberTest1Good(){
    group1.setMemberList(new ArrayList<String>());
    group1.getMemberList().add(userIdGood);
    assertTrue(groupService.isUserMember(userIdGood,groupIdGood));
  }
  @Test public void isUserMemberTest2IsNot(){
    group1.setMemberList(new ArrayList<String>());
    group1.getMemberList().add(userIdGood);
    assertTrue(!groupService.isUserMember(userIdBad,groupIdGood));
  }
  //getAllGroups
  @Test public void getAllGroupsTest1Good(){
    group1.setMemberList(new ArrayList<>());
    group1.getMemberList().add(userIdGood);
    assertTrue(groupService.getAllGroups(userIdGood).contains(group1));
  }


}
