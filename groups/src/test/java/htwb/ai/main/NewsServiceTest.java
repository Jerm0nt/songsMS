package htwb.ai.main;

import htwb.ai.main.model.Group;
import htwb.ai.main.model.News;
import htwb.ai.main.repository.NewsRepository;
import htwb.ai.main.services.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class NewsServiceTest {
  //test-object
  NewsService newsService;
  //mock-object
  NewsRepository mockNewsRepository;
  //userId
  String userIdGood;
  String userIdBad;
  //News
  News newsGood;
  News newsBad;
  //NewsList
  ArrayList<News> newsArrayList;
  //GroupList
  ArrayList<Group> groupArrayList;
  //Group
  Group group;
  //groupId
  String groupId;

  @BeforeEach public void setUp(){
    newsService = new NewsService();
    mockNewsRepository = Mockito.mock(NewsRepository.class);
    newsService.setRepository(mockNewsRepository);
    userIdGood = "goodUserId";
    userIdBad = "badUserId";
    newsGood = new News();
    newsBad = new News();
    newsArrayList = new ArrayList<>();
    groupArrayList = new ArrayList<>();
    group = new Group();
    groupId = "groupId";
    newsGood.setGroupId(groupId);
    group.setGroupId(groupId);
    groupArrayList.add(group);
    newsArrayList.add(newsGood);


    when(mockNewsRepository.findAll()).thenReturn(newsArrayList);
  }
  @Test public void postNewsTest1Good(){
    newsService.postNews(userIdGood, newsGood);
    assertTrue(newsGood.getCreatorId().equals(userIdGood));
    Mockito.verify(mockNewsRepository).save(newsGood);
  }
  @Test public void getAllNewsTest1Good(){
    assertTrue(newsService.getAllNews(groupArrayList).contains(newsGood));
  }
  @Test public void getNewsByGroupIdTest1Good(){
    assertTrue(newsService.getNewsByGroupId(groupId).contains(newsGood));
  }
}
