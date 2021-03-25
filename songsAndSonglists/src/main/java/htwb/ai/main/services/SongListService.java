package htwb.ai.main.services;

import htwb.ai.main.model.Songs;
import htwb.ai.main.model.SongList;
import htwb.ai.main.repository.SongListRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SongListService implements ISongListService {
  @Autowired
  ISongsService songsService;
  @Autowired
  SongListRepository repository;

  @Override
  public int postSongList(SongList songList, String userId) throws NotFoundException {
    List<Songs> songs = songList.getSongList();
    if(songsService.areSongsValid(songs)) {
      songList.setUserId(userId);
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
  public void deleteSongList(Integer id) throws NotFoundException {
    try{
      repository.delete(getSongList(id));
    }catch(Exception e){
      throw new NotFoundException("Songliste nicht gefunden!");
    }
  }

  @Override
  public Set<SongList> getSongListSetByUser(String userId, boolean privateAudience) throws NotFoundException {
    try{
      ArrayList<SongList> allSongList = (ArrayList<SongList>) repository.findAll();
      Set<SongList> userSongList = new HashSet<>();
      for (SongList s : allSongList){
        if(s.getUserId().equals(userId)&&!(s.isPrivate()==true&&privateAudience==false)){
          userSongList.add(s);
        }
      }
      return userSongList;
    }catch(Exception e){
      throw new NotFoundException("Nicht gefunden!");
    }
  }

  @Override
  public void updateSongList(SongList songList) throws NotFoundException {

      List<Songs> songs = songList.getSongList();
      if(songsService.areSongsValid(songs)){
        try{
          repository.findById(songList.getId());
          repository.save(songList);
        }catch(Exception e){
          throw new NotFoundException("Songliste nicht gefunden!");
        }
      }else{
        throw new NotFoundException("Diese Songs sind teilweise nicht in der DB!");
      }

  }
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

  public void setRepositoryAndService(SongListRepository mockSongListRepository, SongsService mockSongsService) {
    this.repository = mockSongListRepository;
    this.songsService = mockSongsService;
  }
}
