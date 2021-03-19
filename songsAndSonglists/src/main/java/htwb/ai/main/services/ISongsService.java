package htwb.ai.main.services;

import htwb.ai.main.model.Songs;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public interface ISongsService {
  ArrayList<Songs> findAll() throws NotFoundException;

  Songs getSong(Integer id) throws NotFoundException;

  int postSong(Songs song) throws Exception;

  void putSong(Integer id, Songs song) throws NotFoundException;

  void deleteSong(Integer id) throws NotFoundException;

  boolean areSongsValid(List<Songs> songs);
}
