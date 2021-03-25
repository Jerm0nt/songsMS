package htwb.ai.main;

import htwb.ai.main.model.User;
import htwb.ai.main.repository.UserRepository;
import htwb.ai.main.services.UserService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
  //test-object
  UserService userService;
  //mock-object
  UserRepository mockUserRepository;
  //userId
  String userIdGood;
  String userIdBad;
  //user
  User userGood;
  User userBad;
  //token
  String tokenGood;
  String tokenBad;
  //userList
  ArrayList<User> userList;


  @BeforeEach public void setUp(){
    userService = new UserService();
    mockUserRepository = Mockito.mock(UserRepository.class);
    userService.setRepository(mockUserRepository);

    userIdGood = "goodUser";
    userIdBad = "heroinUser";

    userGood = new User();
    userGood.setUserId(userIdGood);

    userBad = new User();
    userBad.setUserId(userIdBad);

    tokenGood = "guterToken";
    tokenBad = "boeserToken";

    userGood.setToken(tokenGood);

    userList = new ArrayList<>();
    userList.add(userGood);

    when(mockUserRepository.findById(userIdGood)).thenReturn(Optional.of(userGood));
    when(mockUserRepository.findById(userIdBad)).thenThrow(EntityNotFoundException.class);
    when(mockUserRepository.findAll()).thenReturn(userList);
  }

  //getUserByUserId
  @Test public void getUserByUserIdTest1Good() throws NotFoundException {
    assertTrue(userService.getUserByUserId(userIdGood).getUserId().equals(userIdGood));
  }
  @Test public void getUserByUserIdTest2NotFound(){
    try{
      userService.getUserByUserId(userIdBad);
    }catch (Exception e){
      assertTrue(e.getMessage().equals("Kein User mit dieser ID!"));
    }
  }
  //setToken
  @Test public void setTokenTest1Good() throws NotFoundException {
    userService.setToken(userGood,tokenGood);
    Mockito.verify(mockUserRepository).findById(userIdGood);
    Mockito.verify(mockUserRepository).save(any());
  }
  @Test public void setTokenTest2NotFound(){
    try{
      userService.setToken(userBad, tokenGood);
    }catch (Exception e){
      assertTrue(e.getMessage().equals("Kein solcher User!"));
    }
  }
  //isTokenValid
  @Test public void isTokenValidTest1Good(){
    assertTrue(userService.isTokenValid(tokenGood));
  }
  @Test public void isTokenValidTest2Bad(){
    assertTrue(!userService.isTokenValid(tokenBad));
  }
  //getUserByToken
  @Test public void getUserByTokenTest1Good() throws NotFoundException {
    assertTrue(userService.getUserByToken(tokenGood).getUserId().equals(userIdGood));
  }
  @Test public void getUserByTokenTest2Bad(){
    try{
      userService.getUserByToken(tokenBad);
    }catch(Exception e){
      e.getMessage().equals("Nutzer mit diesem token existiert nicht/konnte nicht gefunden werden!");
    }
  }
}
