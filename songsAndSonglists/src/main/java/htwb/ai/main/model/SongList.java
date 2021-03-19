package htwb.ai.main.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class SongList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_song_list")
  private int id;

  private String userId;

  private String name;

  @JsonProperty(value="isPrivate")
  boolean isPrivate;

  @ManyToMany
  List<Songs> songList;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String user) {
    this.userId = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  public List<Songs> getSongList() {
    return songList;
  }

  public void setSongList(List<Songs> songList) {
    this.songList = songList;
  }
}
