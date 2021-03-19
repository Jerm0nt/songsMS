package htwb.ai.main.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthRestWrapper {
  private RestTemplate restTemplate;

  public AuthRestWrapper() { restTemplate = new RestTemplate();}

  public Boolean isTokenValid(String token) {
    return restTemplate.getForObject("http://localhost:9002/auth/valid/"+token, Boolean.class);
  }

  public Boolean doTokenAndIdMatch(String token, String userId){
    return restTemplate.getForObject("http://localhost:9002/auth/match?token="+token+"&userId="+userId, Boolean.class);
  }


  public String getUserIdByToken(String token) throws NotFoundException {
    String userID = restTemplate.getForObject("http://localhost:9002/auth/getId/"+token, String.class);
    if(userID.equals("User not found!")){
      throw new NotFoundException("kein solcher User!");
    }
    return userID;
  }

  public boolean isUserValid(String userId) {
    return restTemplate.getForObject("http://localhost:9002/auth/validUser/"+userId, Boolean.class);
  }
}
