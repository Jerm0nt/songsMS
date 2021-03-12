package htwb.ai.services;

import htwb.ai.model.SongList;
import htwb.ai.model.User;

import java.util.Set;

public interface IUserInterface {

  boolean isTokenValid(String token);

  User getUserByToken(String token);

  Set<SongList> getSongListSet(String userId, String token);
}
