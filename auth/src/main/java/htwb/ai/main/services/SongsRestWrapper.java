package htwb.ai.main.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SongsRestWrapper {
  private RestTemplate restTemplate;

  public SongsRestWrapper() {this.restTemplate = new RestTemplate();}


}
