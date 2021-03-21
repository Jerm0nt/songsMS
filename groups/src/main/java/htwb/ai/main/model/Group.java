package htwb.ai.main.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name="song_group")
public class Group {

  @Id
  private String groupId;

  private String owner;

  private String description;

  @ElementCollection
  private ArrayList<String> memberList;

  @Enumerated(EnumType.STRING)
  private Genre genre;

  @CreatedDate
  @Column(name = "founding_date", nullable = false, updatable = false)
  //@Temporal(TemporalType.TIMESTAMP)
  private Date foundingDate;

  @Id
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ArrayList<String> getMemberList() {
    return memberList;
  }

  public void setMemberList(ArrayList<String> memberList) {
    this.memberList = memberList;
  }

  public Genre getGenre() {
    return genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }

  public Date getFoundingDate() {
    return foundingDate;
  }

  public void setFoundingDate(Date foundingDate) {
    this.foundingDate = foundingDate;
  }
}
