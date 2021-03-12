package htwb.ai.services;

import htwb.ai.model.SongList;
import htwb.ai.model.Songs;
import htwb.ai.model.User;
import htwb.ai.repository.SongListRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SongListService implements ISongListService {
  @Autowired
  IUserService userService;
  @Autowired
  ISongsService songsService;
  @Autowired
  SongListRepository repository;

  public void deleteSongFromSongLists(Songs songs) {
    ArrayList<SongList> songLists = (ArrayList<SongList>) repository.findAll();
    for (SongList s : songLists){
      for(int i = 0; i<s.getSongList().size(); i++){
        Songs so = s.getSongList().get(i);
        if(songs.getId()==so.getId()){
          s.getSongList().remove(i);
        }
      }
      repository.save(s);
    }

  }


  @Override
  public int postSongList(SongList songList, String token) throws NotFoundException {
    List<Songs> songs = songList.getSongList();
    if(songsService.areSongsValid(songs)) {
      User owner = userService.getUserByToken(token);
      songList.setUser(owner);
      repository.save(songList);
      return songList.getId();
    }else{
      throw new NotFoundException("Diese Songs sind teilweise nicht in der DB!");
    }
  }

  @Override
  public SongList getSongList(Integer id) throws NotFoundException {
    try{
      SongList songList = repository.findById(id).get();
      return songList;
    }catch(Exception e){
      throw new NotFoundException("songList nicht gefunden");
    }
  }

  @Override
  public boolean deleteSongList(Integer id, String token) throws NotFoundException {
    try{
      if(repository.findById(id).get().getUser().getToken().equals(token)){
        userService.deleteSongListFromUser(userService.getUserByToken(token), getSongList(id));
        //getSongList(id).getSongList().clear();
        //repository.deleteById(id);
        repository.delete(getSongList(id));
        return true;
      }else{
        return false;
      }
    }catch(Exception e){
      throw new NotFoundException("Songliste nicht gefunden!");
    }
  }
}
