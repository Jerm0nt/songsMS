package htwb.ai.main.services;

import javassist.NotFoundException;

import java.util.ArrayList;

public interface IGroupService {
  ArrayList findAll() throws NotFoundException;
}
