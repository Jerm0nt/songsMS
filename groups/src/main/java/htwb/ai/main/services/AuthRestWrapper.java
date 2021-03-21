package htwb.ai.main.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthRestWrapper {
  private RestTemplate restTemplate;

  private final String PATH = "http://localhost:8080/auth/";

  public AuthRestWrapper() { restTemplate = new RestTemplate();}

  public Boolean isTokenValid(String token) {
    if(restTemplate.getForObject(PATH + "valid/"+token, Integer.class)==1){
      return true;
    }else{ return false; }
  }

  public Boolean doTokenAndIdMatch(String token, String userId){
    if(restTemplate.getForObject(PATH + "match?token="+token+"&userId="+userId, Integer.class)==1) {
      return true;
    }else{ return false; }
  }


  public String getUserIdByToken(String token) throws NotFoundException {
    String userID = restTemplate.getForObject(PATH + "getId/"+token, String.class);
    if(userID.equals("User not found!")){
      throw new NotFoundException("kein solcher User!");
    }
    return userID;
  }

  public boolean isUserValid(String userId) {
    if(restTemplate.getForObject(PATH + "validUser/"+userId, Integer.class)==1){
      return true;
    }
    else { return false; }
  }
}
