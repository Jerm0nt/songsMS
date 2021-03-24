package htwb.ai.main;

import htwb.ai.main.model.SongList;
import htwb.ai.main.model.Songs;
import htwb.ai.main.repository.SongsRepository;
import htwb.ai.main.services.ISongListService;
import htwb.ai.main.services.SongListService;
import htwb.ai.main.services.SongsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class SongsServiceTest {
  //test-object
  SongsService songsService;
  //mock-objects
  SongsRepository mockSongsRepository;
  //Songs in DB
  Songs songInDb1;
  Songs songInDb2;
  Songs songInDb3;
  //New Songs
  Songs newSong1;
  Songs newSong2NoTitle;
  //Not Valid Song
  Songs notValidSong;
  //SongList
  SongList songList;
  //List with Songs
  ArrayList<Songs> listWithSongs;
  ArrayList<Songs> listWithNotValidSongs;
  //ids
  Integer existingId;
  Integer notExistingId;

  @BeforeEach public void setUp(){
    SongsService songsService = new SongsService();
    mockSongsRepository = Mockito.mock(SongsRepository.class);
    songsService.setRepository(mockSongsRepository);

    songInDb1 = new Songs();
    songInDb1.setArtist("Artist1");
    songInDb1.setLabel("Label1");
    songInDb1.setReleased(1);
    songInDb1.setTitle("Title1");
    songInDb1.setId(1);

    songInDb2 = new Songs();
    songInDb2.setArtist("Artist2");
    songInDb2.setLabel("Label2");
    songInDb2.setReleased(2);
    songInDb2.setTitle("Title2");
    songInDb2.setId(2);

    songInDb3 = new Songs();
    songInDb3.setArtist("Artist3");
    songInDb3.setLabel("Label3");
    songInDb3.setReleased(3);
    songInDb3.setTitle("Title3");
    songInDb3.setId(3);

    newSong1 = new Songs();
    newSong1.setTitle("TitleNew");
    newSong1.setArtist("ArtistNew");
    newSong1.setReleased(4);
    newSong1.setLabel("NewLable");

    newSong2NoTitle.setLabel("NewLableNoTitle");
    newSong2NoTitle.setReleased(5);
    newSong2NoTitle.setArtist("NewArtistNoTitle");

    notValidSong.setArtist("NotValidArtist");
    notValidSong.setTitle("NotValidTitle");
    notValidSong.setReleased(6);
    notValidSong.setLabel("NotValidLable");

    songList = new SongList();
    songList.getSongList().add(songInDb1);

    listWithSongs = new ArrayList<Songs>();
    listWithSongs.add(songInDb1);
    listWithSongs.add(songInDb2);
    listWithSongs.add(songInDb3);

    listWithNotValidSongs = new ArrayList<Songs>();
    listWithNotValidSongs.add(notValidSong);

    existingId = 1;
    notExistingId = 100;

    when(mockSongsRepository.findAll()).thenReturn(listWithSongs);
    when(mockSongsRepository.findById(existingId).get()).thenReturn(songInDb1);
    when(mockSongsRepository.findById(notExistingId).get()).thenThrow(Exception.class);
    when(mockSongsRepository.save(newSong2NoTitle)).thenThrow(Exception.class);
    when(mockSongsRepository.findById(notValidSong.getId())).thenThrow(Exception.class);
    doThrow(Exception.class).when(mockSongsRepository).deleteById(notExistingId);
  }

  //findAll-Test
  @Test public void findAllTest1Good(){
  }
  @Test public void findAllTest2NotFound(){
  }
  //getSong-Test
  @Test public void getSongTest1Good(){
  }
  @Test public void getSongTest2NotFound(){
  }
  //postSong-Test
  @Test public void postSongTest1Good(){
  }
  @Test public void postSongTest2NoTitle(){
  }
  //putSong-Test
  @Test public void putSongTest1Good(){
  }
  @Test public void putSongTest2NoTitle(){
  }
  @Test public void putSongTest3NotMatchingId(){
  }
  @Test public void putSongTest4IdNotFound(){
  }
  //deleteSong-Test
  @Test public void deleteSongTest1Good(){
  }
  @Test public void deleteSongTest2IdNotFound(){
  }
  //areSongsValid-Test
  @Test public void areSongsValidTest1Valid(){
  }
  @Test public void areSongsValidTest2NotValid(){
  }
}
