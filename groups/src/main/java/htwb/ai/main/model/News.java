package htwb.ai.main.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Date creationDate;

  private String groupId;

  private String creatorId;

  private int songListId;

  private String message;

  public int getId() {
    return id;
  }

  public void setId(int newsId) {
    this.id = newsId;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public int getSongListId() {
    return songListId;
  }

  public void setSongListId(int songListId) {
    this.songListId = songListId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
