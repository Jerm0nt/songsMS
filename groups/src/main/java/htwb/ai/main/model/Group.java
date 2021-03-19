package htwb.ai.main.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table
public class Group {

  @Id
  private String groupId;

  private String description;

  @OneToMany
  private ArrayList<String> memberList;

  private Genre genre;

  private Date foundingDate;

  @Id
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
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
