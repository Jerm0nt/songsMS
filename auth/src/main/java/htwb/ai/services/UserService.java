package htwb.ai.services;

import htwb.ai.model.SongList;
import htwb.ai.model.User;
import htwb.ai.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements IUserService {

  @Autowired
  private UserRepository repository;

  @Override
  public User getUserByUserId(String userId) throws NotFoundException {
    try{
      User user = repository.findById(userId).get();
      return user;
    }catch (Exception e){
      throw new NotFoundException("Kein User mit dieser ID!");
    }
  }

  @Override
  public void setToken(User user, String token) throws NotFoundException {
    try{
      User oldUser = repository.findById(user.getUserId()).get();
      oldUser.setToken(token);
      repository.save(oldUser);
    }catch (Exception e){
      throw new NotFoundException("Kein solcher User!");
    }
  }

  @Override
  public boolean isTokenValid(String token){
    ArrayList<User> userList = (ArrayList<User>) repository.findAll();
    for (User u: userList){
      if(u.getToken()!=null) {
        if (u.getToken().equals(token)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public User getUserByToken(String token) throws NotFoundException {
    ArrayList<User> userList = (ArrayList<User>) repository.findAll();
    for (User u: userList){
      if(u.getToken()!=null) {
        if (u.getToken().equals(token)) {
          return u;
        }
      }
    }
    throw new NotFoundException("Nutzer mit diesem token existiert nicht/konnte nicht gefunden werden!");
  }
  @Override
  public Set<SongList> getSongListSet(String userId, String token) throws NotFoundException {
    try{
      User user = repository.findById(userId).get();
      if(token.equals(user.getToken())){
        return user.getSongLists();
      }else{
        Set<SongList> songListSet = user.getSongLists();
        Set<SongList> publicSongLists = new HashSet<>();
        for (SongList s : songListSet){
          if(!s.isPrivate()){
            publicSongLists.add(s);
          }
        }
        return publicSongLists;
      }
    }catch (Exception e){
      throw new NotFoundException("User mit Id nicht gefunden");
    }
  }

  @Override
  public void deleteSongListFromUser(User userByToken, SongList songList) {
    ArrayList<SongList> songLists = new ArrayList<SongList>(userByToken.getSongLists());
    for(int i=0; i<songLists.size(); i++){
      if(songList.getId()==songLists.get(i).getId()){
        songLists.remove(i);
      }
    }

    userByToken.setSongLists(new HashSet(songLists));
    repository.save(userByToken);
  }
}
