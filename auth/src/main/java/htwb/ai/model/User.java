package htwb.ai.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="song_user")
public class User {

  @Id
  private String userId;


  private Set<SongList> songLists = new HashSet<>();

  @OneToMany(mappedBy = "user",
    cascade = CascadeType.ALL)
  public Set<SongList> getSongLists() {
    return songLists;
  }


  public void setSongLists(Set<SongList> songLists) {
    this.songLists = songLists;

    for(SongList s : songLists){
      s.setUser(this);
    }
  }

  private String password;
  private String firstName;
  private String lastName;
  private String token;

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Id
  public String getUserId() {
    return userId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
