package htwb.ai.main;

import com.google.gson.Gson;
import htwb.ai.main.controller.UserController;
import htwb.ai.main.model.User;
import htwb.ai.main.services.UserService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
  //test-object
  UserController userController;
  //mock-objects
  UserService mockUserService;
  //User
  User user1;
  User user2;
  //userIds
  String userId1;
  String userId2;
  //passwords
  String password1;
  String password2;
  //json
  String user1json;
  String user2json;
  //gson
  Gson gson;
  //token
  String tokenValid;
  String tokenInvalid;

  @BeforeEach public void setUp() throws NotFoundException {
    userController = new UserController();
    mockUserService = Mockito.mock(UserService.class);
    userController.setUserService(mockUserService);

    user1 = new User();
    user2 = new User();

    userId1 = "userId1";
    userId2 = "userId2";

    password1 = "password1";
    password2 = "password2";

    user1.setUserId(userId1);
    user2.setUserId(userId2);

    user1.setPassword(password1);
    user2.setPassword(password2);

    gson = new Gson();

    user1json = gson.toJson(user1,User.class);
    user2json = gson.toJson(user2,User.class);

    tokenValid = "tokenValid";
    tokenInvalid = "tokenInvalid";

    when(mockUserService.getUserByUserId(userId1)).thenReturn(user1);
    when(mockUserService.getUserByUserId(userId2)).thenThrow(NotFoundException.class);
    when(mockUserService.getUserByToken(tokenValid)).thenReturn(user1);
    when(mockUserService.getUserByToken(tokenInvalid)).thenThrow(NotFoundException.class);
  }
  //authorize
  @Test public void authorizeTest1Good() throws IOException {
    assertTrue(userController.authorize(user1json).getStatusCode().equals(HttpStatus.OK));
  }
  @Test public void authorizeTest2WrongPW() throws IOException {
    user1.setPassword(password2);
    assertTrue(userController.authorize(user1json).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
  }
  @Test public void authorizeTest3NotFound() throws IOException {
    assertTrue(userController.authorize(user2json).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    assertTrue(userController.authorize(user2json).getBody().equals("Declined: No such user!"));
  }
  @Test public void authorizeTest4BadJson() throws IOException {
    assertTrue(userController.authorize("Kein Json leider!").getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }
  //isTokenValid
  @Test public void isTokenValidTest1Good(){
    assertTrue(userController.isTokenValid(tokenValid));
  }
  @Test public void isTokenValidTest2NotValid(){
    assertTrue(!userController.isTokenValid(tokenInvalid));
  }
  //doTokenAndIdMatch
  @Test public void doTokenAndIdMatchTest1Good(){
    assertTrue(userController.doTokenAndIdMatch(userId1,tokenValid));
  }
  @Test public void doTokenAndIdMatchTest2false(){
    assertTrue(!userController.doTokenAndIdMatch("user3",tokenValid));
  }
  @Test public void doTokenAndIdMatchTest3NotFound(){
      assertTrue(!userController.doTokenAndIdMatch(userId2,tokenValid));
  }
  //isUserValid
  @Test public void isUserValidTest1Good(){
    assertTrue(userController.isUserValid(userId1));
  }
  @Test public void isUserValidTest2NotFound(){
    assertTrue(!userController.isUserValid(userId2));
  }
  //getUserIdByToken
  @Test public void getUserIdByTokenTest1Good(){
    assertTrue(userController.getUserIdByToken(tokenValid).equals(userId1));
  }
  @Test public void getUserIdByTokenTest2NotFound(){
    assertTrue(userController.getUserIdByToken(tokenInvalid).equals("User not found!"));
  }
}
