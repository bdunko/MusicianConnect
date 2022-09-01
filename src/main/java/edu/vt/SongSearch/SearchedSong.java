/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.SongSearch;

public class SearchedSong {
    //Instance variables
    private int id;
    private String title;
    private String artist;
    private String lyrics;
    private String tabs_link;
    private String youtube_video_id;

    //Constructor
    public SearchedSong(int id, String title, String artist, String lyrics, String tabs_link, String youtube_video_id) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
        this.tabs_link = tabs_link;
        this.youtube_video_id = youtube_video_id;
    }

    //Getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
