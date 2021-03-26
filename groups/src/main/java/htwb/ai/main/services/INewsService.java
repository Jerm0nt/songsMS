package htwb.ai.main.services;

import htwb.ai.main.model.Group;
import htwb.ai.main.model.News;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface INewsService {
  int postNews(String userId, News news);

  ArrayList<News> getAllNews(ArrayList<Group> groupList);

  ArrayList<News> getNewsByGroupId(String groupId);
}
