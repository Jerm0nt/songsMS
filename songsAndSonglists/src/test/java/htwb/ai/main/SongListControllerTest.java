package htwb.ai.main;

import com.google.gson.Gson;
import htwb.ai.main.controller.SongListController;
import htwb.ai.main.model.SongList;
import htwb.ai.main.services.AuthRestWrapper;
import htwb.ai.main.services.ISongListService;
import htwb.ai.main.services.SongListService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class SongListControllerTest {
  //class-objects
  ISongListService mockSongListService;
  AuthRestWrapper mockAuthRestWrapper;
  SongListController songListController;
  //ids
  int testIdExistent;
  int testIdNichtExistent;
  //location
  String testLocation;
  //token
  String tokenValid;
  String tokenInvalid;
  String tokenEmpty;
  //Json
  String testJSONBodyGood;
  String testJSONBodyNoTitle;
  String testJSONBodyBad;
  String returnStringJSON;
  String testJSONBodyWrongSong;
  //SongList
  SongList testSongList;
  SongList testSongListWrongSong;
  //userId
  String authorizedUserId;


  @BeforeEach
  public void setup() throws NotFoundException {

    mockSongListService = Mockito.mock(SongListService.class);
    mockAuthRestWrapper = Mockito.mock(AuthRestWrapper.class);
    songListController = new SongListController();
    songListController.setServices(mockSongListService,mockAuthRestWrapper);

    testIdExistent = 1;
    testIdNichtExistent = 200;
    tokenValid = "validerToken";
    tokenEmpty = "";

    authorizedUserId="eschuler";
    when(mockAuthRestWrapper.isTokenValid(tokenValid)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenInvalid)).thenReturn(false);
    when(mockAuthRestWrapper.isTokenValid(tokenEmpty)).thenReturn(false);
    when(mockAuthRestWrapper.doTokenAndIdMatch(tokenValid,authorizedUserId)).thenReturn(true);

    Gson gson = new Gson();

    testJSONBodyGood = "{ \"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    testJSONBodyWrongSong = "{ \"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"Peter Maffay\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    testJSONBodyNoTitle = "{ \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    testJSONBodyBad = "Bei Gott kein JSON_Body";
    returnStringJSON = "{ \"id\": 7,\"userId\": \"eschuler\",\"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";

    testSongList = gson.fromJson(returnStringJSON, SongList.class);
    testSongListWrongSong = gson.fromJson(testJSONBodyWrongSong, SongList.class);
    when(mockSongListService.getSongList(testIdExistent)).thenReturn(testSongList);
    when(mockSongListService.getSongList(testIdNichtExistent)).thenThrow(NotFoundException.class);
    when(mockSongListService.postSongList(testSongListWrongSong, authorizedUserId)).thenThrow(NotFoundException.class);

  }

  //getSongListByIdTest
  @Test public void getSongListTest1Good(){
    assertTrue(songListController.getSongListById(testIdExistent, tokenValid).getStatusCode().equals(HttpStatus.OK));
    SongList test = (SongList) songListController.getSongListById(testIdExistent, tokenValid).getBody();
    Assertions.assertTrue(test.getName().equals(testSongList.getName()));
  }
  @Test public void getSongListTest2BadId(){
    assertTrue(songListController.getSongListById(testIdNichtExistent, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  @Test public void getSongListTest3InvalidToken(){
    assertTrue(songListController.getSongListById(testIdExistent, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
}
