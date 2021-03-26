package htwb.ai.main.services;

import htwb.ai.main.model.Group;
import htwb.ai.main.repository.GroupRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class GroupService implements IGroupService {

  @Autowired
  private GroupRepository repository;

  @Override
  public ArrayList findAll() throws NotFoundException {
    try{return (ArrayList<Group>) repository.findAll();}
    catch(Exception e){throw new NotFoundException("Keine Groups");}
  }

  @Override
  public void postGroup(Group group, String userId) {
      ArrayList<Group> allGroups = (ArrayList<Group>) repository.findAll();
      for (Group g : allGroups){
        if(g.getGroupId().equals(group.getGroupId())){
          throw new InvalidParameterException("GroupId existiert bereits");
        }
      }
      if(group.getMemberList()!=null){
        throw new InvalidParameterException("Memberliste muss leer sein!");
      }
      group.setOwner(userId);
      group.setFoundingDate(new Date(System.currentTimeMillis()));
      group.setMemberList(new ArrayList<>());
      group.getMemberList().add(userId);
      repository.save(group);
  }

  @Override
  public Group getGroupById(String groupId) throws NotFoundException {
    try{
      return repository.findById(groupId).get();
    }catch(Exception e){
      throw new NotFoundException("Keine Gruppe mit dieser ID!");
    }
  }

  @Override
  public void addMember(String groupId, String userId) {
    Group group = repository.findById(groupId).get();
    for(String groupMember : group.getMemberList()){
      if(groupMember.equals(userId)){
        throw new InvalidParameterException("Nutzer schon in Gruppe!");
      }
    }
    group.getMemberList().add(userId);
    repository.save(group);
  }

  @Override
  public void leaveGroup(String groupId, String userId) throws NotFoundException {
    Group group;
    try{
      group = repository.findById(groupId).get();
    }catch(Exception e){
      throw new NotFoundException("Gruppe nicht gefunden!");
    }
    if(group.getOwner().equals(userId)){
      throw new InvalidParameterException("Owner kann nicht aus Gruppe austreten!");
    }
    if(!group.getMemberList().contains(userId)){
      throw new NotFoundException("User nicht in dieser Gruppe!");
    }
    group.getMemberList().remove(userId);
    repository.save(group);


  }

  @Override
  public boolean isUserMember(String userId, String groupId){
    try{
      return getGroupById(groupId).getMemberList().contains(userId);
    }catch (NotFoundException e){
      return false;
    }
  }

  @Override
  public ArrayList<Group> getAllGroups(String userId) {
    ArrayList<Group> allGroups = (ArrayList<Group>) repository.findAll();
    ArrayList<Group> userGroups = new ArrayList<>();
    for(Group g : allGroups){
      if(g.getMemberList().contains(userId)){
        userGroups.add(g);
      }
    }
    return userGroups;
  }

  public void setRepository(GroupRepository mockGroupRepository){
    this.repository = mockGroupRepository;
  }
}
