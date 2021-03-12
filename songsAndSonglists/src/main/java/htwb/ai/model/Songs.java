package htwb.ai.model;

import javax.persistence.*;

@Entity
public class Songs {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String title;

  private String artist;

  private String label;

  private int released;

  public Songs(){}

  public int getId(){
    return id;
  }

  public String getTitle(){
    return title;
  }
  public void setTitle(String title){ this.title=title;
  }

  public String getArtist(){

    if(this.artist!=null){
      return artist;
    }else{
      return "";
    }
  }
  public void setArtist(String artist){
    this.artist=artist;
  }

  public String getLabel(){
    if (this.label != null) {
      return label;
    }else{
      return "";
    }
  }
  public void setLabel(String label){
    this.label=label;
  }

  public int getReleased(){
    return released;
  }

  public void setReleased(int released){
    this.released=released;
  }

  public boolean equals(Songs comparedSong){
    return (this.getId()==comparedSong.getId()
      && this.getTitle().equals(comparedSong.getTitle())
      && this.getReleased() == comparedSong.getReleased()
      && this.getArtist().equals(comparedSong.getArtist())
      && this.getLabel().equals(comparedSong.getLabel()));
  }
}
