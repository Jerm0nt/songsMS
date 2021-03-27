package htwb.ai.main;

import htwb.ai.main.controller.NewsController;
import htwb.ai.main.model.Group;
import htwb.ai.main.model.News;
import htwb.ai.main.services.AuthRestWrapper;
import htwb.ai.main.services.GroupService;
import htwb.ai.main.services.NewsService;
import htwb.ai.main.services.SongListRestWrapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class NewsControllerTest {
  //test-object
  NewsController newsController;
  //mockObjekte
  NewsService mockNewsService;
  GroupService mockGroupService;
  AuthRestWrapper mockAuthRestWrapper;
  SongListRestWrapper mockSongListRestWrapper;
  //News
  News newsInDB;
  News newNewsGood;
  News newNewsBad;
  //token
  String tokenOwnerValid;
  String tokenMemberValid;
  String tokenBadUserValid;
  String tokenInvalid;
  //groupId
  String groupIdGood;
  String groupIdBad;
  //userIds
  String userIdOwner;
  String userIdMember;
  String userIdBad;
  //News-Attributes
  String message;
  int songListIdPrivate;
  int songListIdPublic;
  int newsIdGood;
  //Lists
  ArrayList<Group> groupArrayList;
  ArrayList<News> newsArrayList;
  //group
  Group group;

  @BeforeEach public void setUp() throws NotFoundException {
    //test-object
    newsController = new NewsController();
    //mockObjekte
    mockNewsService = Mockito.mock(NewsService.class);
    mockGroupService = Mockito.mock(GroupService.class);
    mockAuthRestWrapper = Mockito.mock(AuthRestWrapper.class);
    mockSongListRestWrapper = Mockito.mock(SongListRestWrapper.class);
    //News
    newsInDB = new News();
    newNewsGood = new News();
    newNewsBad = new News();
    //token
    tokenOwnerValid = "validOwner";
    tokenMemberValid = "validMember";
    tokenBadUserValid = "validBadUser";
    tokenInvalid = "invalid";
    //groupId
    groupIdGood = "goodGroup";
    groupIdBad = "badGroup";
    //userIds
    userIdOwner = "goodOwner";
    userIdMember = "goodMember";
    userIdBad = "badUser";
    //News-Attributes
    message = "message";
    songListIdPrivate = 1;
    songListIdPublic = 2;
    newsIdGood = 1;
    //Lists
    groupArrayList = new ArrayList<>();
    newsArrayList = new ArrayList<>();
    //group
    group = new Group();
    //settings
    groupArrayList.add(group);
    newsArrayList.add(newsInDB);
    newNewsGood.setGroupId(groupIdGood);
    newNewsGood.setSongListId(songListIdPublic);

    newsController.setServices(mockNewsService,mockGroupService,mockAuthRestWrapper,mockSongListRestWrapper);

    when(mockAuthRestWrapper.isTokenValid(tokenMemberValid)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenOwnerValid)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenBadUserValid)).thenReturn(true);
    when(mockAuthRestWrapper.isTokenValid(tokenInvalid)).thenReturn(false);

    when(mockAuthRestWrapper.getUserIdByToken(tokenMemberValid)).thenReturn(userIdMember);
    when(mockAuthRestWrapper.getUserIdByToken(tokenOwnerValid)).thenReturn(userIdOwner);
    when(mockAuthRestWrapper.getUserIdByToken(tokenBadUserValid)).thenReturn(userIdBad);

    when(mockGroupService.isUserMember(userIdMember, groupIdGood)).thenReturn(true);
    when(mockGroupService.isUserMember(userIdOwner, groupIdGood)).thenReturn(true);
    when(mockGroupService.isUserMember(userIdBad, groupIdGood)).thenReturn(false);

    when(mockSongListRestWrapper.getOwner(songListIdPrivate)).thenReturn(userIdOwner);
    when(mockSongListRestWrapper.getOwner(songListIdPublic)).thenReturn(userIdOwner);

    when(mockSongListRestWrapper.isSongListPrivate(songListIdPrivate)).thenReturn(true);
    when(mockSongListRestWrapper.isSongListPrivate(songListIdPublic)).thenReturn(false);

    when(mockNewsService.postNews(userIdOwner, newNewsGood)).thenReturn(newsIdGood);

    when(mockGroupService.getAllGroups(userIdMember)).thenReturn(groupArrayList);
    when(mockNewsService.getAllNews(groupArrayList)).thenReturn(newsArrayList);

    when(mockNewsService.getNewsByGroupId(groupIdGood)).thenReturn(newsArrayList);
  }
  @Test public void postNewsTest1Good(){
    assertTrue(newsController.postNews(newNewsGood,tokenOwnerValid).getStatusCode().equals(HttpStatus.OK));
  }
  @Test public void postNewsTest2Unauthorized(){
    assertTrue(newsController.postNews(newNewsGood,tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void postNewsTest3NoGroupId(){
    assertTrue(newsController.postNews(newNewsBad,tokenOwnerValid).getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }
  @Test public void postNewsTest4NoMember(){
    newNewsBad.setGroupId(groupIdBad);
    assertTrue(newsController.postNews(newNewsBad,tokenBadUserValid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void postNewsTest5NotOwner(){
    newNewsBad.setGroupId(groupIdBad);
    assertTrue(newsController.postNews(newNewsBad,tokenMemberValid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void postNewsTest6Private(){
    newNewsGood.setSongListId(songListIdPrivate);
    assertTrue(newsController.postNews(newNewsGood,tokenOwnerValid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  //getAllNews
  @Test public void getAllNewsTest1Good(){
    assertTrue(newsController.getAllNews(tokenOwnerValid).getStatusCode().equals(HttpStatus.OK));
  }
  @Test public void getAllNewsTest2Unauthorized(){
    assertTrue(newsController.getAllNews(tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  //getNewsByGroup
  @Test public void getNewsByGroupTest1Good(){
    assertTrue(newsController.getNewsByGroup(groupIdGood,tokenMemberValid).getStatusCode().equals(HttpStatus.OK));
  }
  @Test public void getNewsByGroupTest2Unauthorized(){
    assertTrue(newsController.getNewsByGroup(groupIdGood,tokenInvalid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void getNewsByGroupTest3NotMember(){
    assertTrue(newsController.getNewsByGroup(groupIdGood,tokenBadUserValid).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    Mockito.verify(mockGroupService).isUserMember(userIdBad, groupIdGood);
  }
}
