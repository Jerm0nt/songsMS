package htwb.ai.main.services;

import htwb.ai.main.model.Group;
import htwb.ai.main.repository.GroupRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class GroupService implements IGroupService {

  @Autowired
  private GroupRepository repository;

  @Override
  public ArrayList findAll() throws NotFoundException {
    try{return (ArrayList<Group>) repository.findAll();}
    catch(Exception e){throw new NotFoundException("Keine Groups");}
  }
}
