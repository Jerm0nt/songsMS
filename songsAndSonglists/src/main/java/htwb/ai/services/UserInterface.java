package htwb.ai.services;

import htwb.ai.model.SongList;
import htwb.ai.model.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserInterface implements IUserInterface {

  @Override
  public boolean isTokenValid(String token) {
    return false;
  }

  @Override
  public User getUserByToken(String token) {
    return null;
  }

  @Override
  public Set<SongList> getSongListSet(String userId, String token) {
    return null;
  }
}
