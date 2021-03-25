package htwb.ai.main.services;


import htwb.ai.main.model.Songs;
import htwb.ai.main.repository.SongsRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongsService implements ISongsService {

  @Autowired
  private SongsRepository repository;

  @Autowired
  private ISongListService songListService;

  @Override
  public ArrayList<Songs> findAll() throws NotFoundException {


    try{return (ArrayList<Songs>) repository.findAll();}
    catch(Exception e){throw new NotFoundException("Keine songs");}
  }

  @Override
  public Songs getSong(Integer id) throws NotFoundException {
    try { return repository.findById(id).get(); }
    catch (Exception e) { throw new NotFoundException("Kein Song mit dieser id"); }
  }
  @Override
  public int postSong(Songs song) throws Exception {
    try{
      repository.save(song);
      return song.getId();
    }catch (Exception e){
      throw new Exception("Title on null is not allowed!");
    }

  }

  @Override
  public void putSong(Integer id, Songs song) throws NotFoundException {
    if(song.getId()!=id || song.getTitle()==null){
      throw new InvalidParameterException("Die übergebenen Parameter sind nicht gültig!");
    }
    try{
      Songs oldSong = repository.findById(id).get();
      oldSong.setArtist(song.getArtist());
      oldSong.setTitle(song.getTitle());
      oldSong.setLabel(song.getLabel());
      oldSong.setReleased(song.getReleased());

      repository.save(oldSong);
    }catch(Exception e){
      throw new NotFoundException("Song mit id existiert nicht");
    }


  }

  @Override
  public void deleteSong(Integer id) throws NotFoundException {
    try{
      songListService.deleteSongFromSongLists(repository.findById(id).get());
      repository.deleteById(id);
    }catch(Exception e){
      e.printStackTrace();
      throw new NotFoundException("song mit id existiert nicht");
    }
  }

  @Override
  public boolean areSongsValid(List<Songs> songs) {
    for(Songs s : songs){
      try{
        if(!s.equals(repository.findById(s.getId()).get())){
          return false;
        }
      }catch(Exception e){
        return false;
      }
    }
    return true;
  }

  public void setRepository(SongsRepository mockSongsRepository, SongListService mockSongListService){
    this.repository = mockSongsRepository;
    this.songListService = mockSongListService;
  }
}
