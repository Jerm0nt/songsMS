package htwb.ai.main.services;

import htwb.ai.main.model.Group;
import javassist.NotFoundException;

import java.util.ArrayList;

public interface IGroupService {
  ArrayList findAll() throws NotFoundException;

  void postGroup(Group group, String userId);

  Group getGroupById(String groupId) throws NotFoundException;

  void addMember(String groupId, String userId);

  void leaveGroup(String groupId, String userIdByToken) throws NotFoundException;

  boolean isUserMember(String userId, String groupId) throws NotFoundException;

  ArrayList<Group> getAllGroups(String userId);
}
