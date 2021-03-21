package htwb.ai.main.services;

import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthRestWrapper {
  private RestTemplate restTemplate;

  private final String PATH = "http://localhost:8080/auth/";

  public AuthRestWrapper() { restTemplate = new RestTemplate();}

  public Boolean isTokenValid(String token) {
    ResponseEntity<Boolean> resp = restTemplate.getForObject(PATH + "valid/"+token, ResponseEntity.class);
    return true;
  }

  public Boolean doTokenAndIdMatch(String token, String userId){
    return (boolean) restTemplate.getForObject(PATH + "match?token="+token+"&userId="+userId, ResponseEntity.class).getBody();
  }


  public String getUserIdByToken(String token) throws NotFoundException {
    String userID = restTemplate.getForObject(PATH + "getId/"+token, String.class);
    if(userID.equals("User not found!")){
      throw new NotFoundException("kein solcher User!");
    }
    return userID;
  }

  public boolean isUserValid(String userId) {
    return (boolean) restTemplate.getForObject(PATH + "validUser/"+userId, ResponseEntity.class).getBody();
  }
}
