package htwb.ai.main.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SongListRestWrapper {
  private RestTemplate restTemplate;
  private final String URL = "http://localhost:9001/songs/playlist";

  public SongListRestWrapper() { restTemplate = new RestTemplate();}

  public String getOwner(int songListId) {
    return restTemplate.getForObject(URL+"/owner/"+songListId, String.class);
  }

  public boolean isSongListPrivate(int songListId) {
    return restTemplate.getForObject(URL+"/isprivate/"+songListId,Boolean.class);
  }
}
