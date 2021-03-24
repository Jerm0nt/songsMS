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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class SongListControllerTest {
  //class-objects
  ISongListService mockSongListService;
  AuthRestWrapper mockAuthRestWrapper;
  SongListController songListController;
  //ids
  int testIdExistent;
  int testIdExistent2;
  int testIdNichtExistent;
  //location
  String testLocation;
  //token
  String tokenValid;
  String tokenValid2;
  String tokenInvalid;
  String tokenEmpty;
  //Json
  String testJSONBodyGood;
  String testJSONBodyNoTitle;
  String testJSONBodyBad;
  String returnStringJSON;
  String testJSONBodyWrongSong;
  String returnStringJSONWrongSong;
  //SongList
  SongList testSongList;
  SongList testSongListWrongSong;
  Set<SongList> testSongListSet;
  //userId
  String authorizedUserId;
  String notValidUserId;


  @BeforeEach
  public void setup() throws NotFoundException {

    mockSongListService = Mockito.mock(SongListService.class);
    mockAuthRestWrapper = Mockito.mock(AuthRestWrapper.class);
    songListController = new SongListController();
    songListController.setServices(mockSongListService, mockAuthRestWrapper);

    //SongListIds
    testIdExistent = 1;
    testIdExistent2 = 2;
    testIdNichtExistent = 200;
    //token
    tokenValid = "validerToken";
    tokenValid2 = "auch valide, anderer Nutzer";
    tokenEmpty = "";
    //userIds
    authorizedUserId = "eschuler";
    notValidUserId = "keinUser";
    testLocation = "http://localhost:8080/songs/playist/";

    Gson gson = new Gson();

    //JsonBodys
    testJSONBodyGood = "{ \"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"isPrivate\": true}";
    testJSONBodyWrongSong = "{ \"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"Peter Maffay\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    testJSONBodyNoTitle = "{ \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    testJSONBodyBad = "Bei Gott kein JSON_Body";
    returnStringJSON = "{ \"id\": 1,\"userId\": \"eschuler\",\"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"MILEY CYRUS\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    returnStringJSONWrongSong = "{ \"id\": 1,\"userId\": \"eschuler\",\"name\": \"Schuler Takeover\", \"songList\": " +
      "[ { \"id\": 2,\"title\": \"Wrecking Ball\",\"artist\": \"Peter Maffay\",\"label\": \"RCA\",\"released\": 2013 }]," +
      "\"private\": true,\"isPrivate\": true}";
    //songLists
    testSongList = gson.fromJson(returnStringJSON, SongList.class);
    testSongListWrongSong = gson.fromJson(returnStringJSONWrongSong, SongList.class);
    testSongListSet = new HashSet<>();
    testSongListSet.add(testSongList);
    //songListServiceMock
    when(mockSongListService.getSongList(testIdExistent)).thenReturn(testSongList);
    when(mockSongListService.getSongList(testIdNichtExistent)).thenThrow(NotFoundException.class);
    when(mockSongListService.postSongList(testSongListWrongSong, authorizedUserId)).thenThrow(NotFoundException.class);
    when(mockSongListService.getSongListSetByUser(authorizedUserId, true)).thenReturn(testSongListSet);
    doThrow(NotFoundException.class).when(mockSongListService).updateSongList(testSongListWrongSong);
    //authRestWrapperMock
    when(mockAuthRestWrapper.getUserIdByToken(tokenValid)).thenReturn(authorizedUserId);
    when(mockAuthRestWrapper.isTokenValid(tokenValid)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenValid2)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenInvalid)).thenReturn(false);
    when(mockAuthRestWrapper.isTokenValid(tokenEmpty)).thenReturn(false);
    when(mockAuthRestWrapper.isUserValid(authorizedUserId)).thenReturn(true);
    when(mockAuthRestWrapper.isUserValid(notValidUserId)).thenReturn(false);
    when(mockAuthRestWrapper.doTokenAndIdMatch(tokenValid, authorizedUserId)).thenReturn(true);
    when(mockAuthRestWrapper.doTokenAndIdMatch(tokenValid2, authorizedUserId)).thenReturn(false);

  }

  //getSongListByIdTest
  @Test public void getSongListByIdTest1Good() {
    assertTrue(songListController.getSongListById(testIdExistent, tokenValid).getStatusCode().equals(HttpStatus.OK));
    SongList test = (SongList) songListController.getSongListById(testIdExistent, tokenValid).getBody();
    Assertions.assertTrue(test.getName().equals(testSongList.getName()));
  }
  @Test public void getSongListTest2BadId() {
    assertTrue(songListController.getSongListById(testIdNichtExistent, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  @Test public void getSongListTest3InvalidToken() {
    assertTrue(songListController.getSongListById(testIdExistent, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void getSongListByIdTest4EmptyToken() {
    assertTrue(songListController.getSongListById(testIdExistent, tokenEmpty).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void getSongListByIdTest5TokenAndIdDontMatch() {
    assertTrue(songListController.getSongListById(testIdExistent, tokenValid2).getStatusCode().equals(HttpStatus.FORBIDDEN));
  }
  //getSongListByUser
  @Test public void getSongListByUserTest1Good() {
    assertTrue(songListController.getSongListByUser(authorizedUserId, tokenValid).getStatusCode().equals(HttpStatus.OK));
  }
  @Test public void getSongListByUserTest2UserNotFound() {
    assertTrue(songListController.getSongListByUser(notValidUserId, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  @Test public void getSongListByUserTest3FalseToken() {
    assertTrue(songListController.getSongListByUser(authorizedUserId,tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  //postSongListTest
  @Test public void postSongListTest1Good() throws NotFoundException {
    when(mockSongListService.postSongList(testSongList, testSongList.getUserId())).thenReturn(testIdExistent);
    assertTrue(songListController.postSongList(testSongList, tokenValid).getStatusCode().equals(HttpStatus.CREATED));
    assertTrue(songListController.postSongList(testSongList, tokenValid).getHeaders().getLocation().toString()
      .equals(testLocation + testIdExistent));
  }
  @Test public void postSongListTest2BadSongs() {
    assertTrue(songListController.postSongList(testSongListWrongSong, tokenValid).getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }
  @Test public void postSongListTest3InvalidToken() {
    assertTrue(songListController.postSongList(testSongList, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  //putSongList-Test
  @Test public void updateSongListTest1Good() {
    assertTrue(songListController.updateSongList(testIdExistent,testSongList,tokenValid).getStatusCode().equals(HttpStatus.NO_CONTENT));
  }
  @Test public void updateSongListTest2BadSongs() {
    assertTrue(songListController.updateSongList(testIdExistent,testSongListWrongSong,tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  @Test public void updateSongListTest3Forbidden() {
    assertTrue(songListController.updateSongList(testIdExistent, testSongList, tokenValid2).getStatusCode().equals(HttpStatus.FORBIDDEN));
  }
  @Test public void updateSongListTest4FalseToken() {
    assertTrue(songListController.updateSongList(testIdExistent, testSongList, tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void updateSongListTest5NotFound() {
    assertTrue(songListController.updateSongList(testIdNichtExistent, testSongList, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  //deleteSongList-Test
  @Test public void deleteSongListTest1Good() {
    assertTrue(songListController.deleteSongList(testIdExistent,tokenValid).getStatusCode().equals(HttpStatus.NO_CONTENT));
  }
  @Test public void deleteSongListTest2IdNotFound() {
    assertTrue(songListController.deleteSongList(testIdNichtExistent, tokenValid).getStatusCode().equals(HttpStatus.NOT_FOUND));
  }
  @Test public void deleteSongListTest3Forbidden() {
    assertTrue(songListController.deleteSongList(testIdExistent,tokenValid2).getStatusCode().equals(HttpStatus.FORBIDDEN));
  }
  @Test public void deleteSongListTest4FalseToken() {
    assertTrue(songListController.deleteSongList(testIdExistent,tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
}
