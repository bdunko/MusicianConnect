/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity

// Name of the database table represented
@Table(name = "PlayableSong")

@NamedQueries({
        // This named query is used in SongFacade
        @NamedQuery(name = "PlayableSong.findByName", query = "SELECT c FROM PlayableSong c WHERE c.title = :title")
})

public class PlayableSong implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     CREATE TABLE PlayableSong
     (
     id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
     user_id INT UNSIGNED,
     title VARCHAR (512) NOT NULL,
     artist VARCHAR (512) NOT NULL,
     genre VARCHAR (64) NOT NULL,
     duration VARCHAR (8),
     lyrics VARCHAR (8000),
     tabs_link VARCHAR (512),
     youtube_video_id VARCHAR (32) NOT NULL,
     FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
     );
     */

    // Primary key for the entry
    // id INT UNSIGNED NOT NULL AUTO_INCREMENT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // User ID
    // user_id INT UNSIGNED NOT NULL
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer user_id;

    // Title of the song
    // title VARCHAR(512) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "title")
    private String title;

    // Song's artist
    // artist VARCHAR (512) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "artist")
    private String artist;

    // Song's genre
    // genre VARCHAR (64) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "genre")
    private String genre;

    // Song duration
    // duration VARCHAR (8)
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "duration")
    private String duration;

    //Lyrics
    //lyrics VARCHAR (8000)
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "lyrics")
    private String lyrics;

    //Link to Songsterr page with tabs
    //tabs_link VARCHAR (512)
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 512)
    @Column(name = "tabs_link")
    private String tabs_link;

    //Youtube Video ID for a music video of this song
    //youtube_video_id VARCHAR (32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 32)
    @Column(name = "youtube_video_id")
    private String youtube_video_id;


    /* Constructors to create an instance of PlayableSong */
    // Used in PrepareCreate method in PlayableSong
    public PlayableSong() {
    }

    /* Getter and Setter methods */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getTabs_link() {
        return tabs_link;
    }

    public void setTabs_link(String tabs_link) {
        this.tabs_link = tabs_link;
    }

    public String getYoutube_video_id() {
        return youtube_video_id;
    }

    public void setYoutube_video_id(String youtube_video_id) {
        this.youtube_video_id = youtube_video_id;
    }

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /*
     Used to compare playable song objects
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlayableSong)) {
            return false;
        }
        PlayableSong other = (PlayableSong) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

}
