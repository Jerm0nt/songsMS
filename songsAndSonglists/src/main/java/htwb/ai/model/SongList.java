package htwb.ai.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class SongList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_song_list")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private User user;

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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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
