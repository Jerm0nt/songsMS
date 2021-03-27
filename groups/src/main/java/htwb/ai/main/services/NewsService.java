package htwb.ai.main.services;

import htwb.ai.main.model.Group;
import htwb.ai.main.model.News;
import htwb.ai.main.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class NewsService implements INewsService{
  @Autowired
  private NewsRepository repository;

  @Override
  public int postNews(String userId, News news) {
    news.setCreatorId(userId);
    news.setCreationDate(new Date(System.currentTimeMillis()));
    repository.save(news);
    return news.getId();
  }

  @Override
  public ArrayList<News> getAllNews(ArrayList<Group> groupList) {
    ArrayList<News> allNews = (ArrayList<News>) repository.findAll();
    ArrayList<News> userNews = new ArrayList<>();
    for(News n : allNews){
      for(Group g : groupList){
        if(n.getGroupId().equals(g.getGroupId())){
          userNews.add(n);
        }
      }
    }
    return userNews;
  }

  @Override
  public ArrayList<News> getNewsByGroupId(String groupId) {
    ArrayList<News> allNews = (ArrayList<News>) repository.findAll();
    ArrayList<News> groupNews = new ArrayList<>();
    for(News n : allNews){
      if(n.getGroupId().equals(groupId)){
        groupNews.add(n);
      }
    }
    return groupNews;
  }

  public void setRepository(NewsRepository mockNewsRepository){
    this.repository = mockNewsRepository;
  }
}
