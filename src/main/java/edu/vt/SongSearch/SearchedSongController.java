/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.SongSearch;

import edu.vt.EntityBeans.PublicSong;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

@Named("searchedSongController")
@SessionScoped
public class SearchedSongController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<SearchedSong> listOfSearchedSongs;

    private String searchQuery;
    private String maxNumberOfSongs;
    private SearchedSong selected;
    private String idOfVideoToPlay;

    /*
     Unselect a selected PlayableSong object
     */
    public void unselect() {
        selected = null;
    }

    /*
    Generated Getter and Setter methods
     */
    public List<SearchedSong> getListOfSearchedSongs() {
        return listOfSearchedSongs;
    }

    public void setListOfSearchedSongs(List<SearchedSong> listOfSearchedSongs) {
        this.listOfSearchedSongs = listOfSearchedSongs;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchedSong getSelected() {
        return selected;
    }

    public void setSelected(SearchedSong selected) {
        this.selected = selected;
    }

    public String getMaxNumberOfSongs() {
        return maxNumberOfSongs;
    }

    public void setMaxNumberOfSongs(String maxNumberOfSongs) {
        this.maxNumberOfSongs = maxNumberOfSongs;
    }

    public String getIdOfVideoToPlay() {
        return idOfVideoToPlay;
    }

    public void setIdOfVideoToPlay(String idOfVideoToPlay) {
        this.idOfVideoToPlay = idOfVideoToPlay;
    }

    /*
    Perform the search using the parameters given by the user
     */
    public String performSearch() {
        selected = null;
        listOfSearchedSongs = new ArrayList<>();

        // Spaces in search query must be replaced with "+"
        searchQuery = searchQuery.replaceAll(" ", "+");

        String songsterrSearchUrl = Methods.replaceSpaces(Constants.SONGSTERR_BASE_URL + searchQuery);

        try {
            JSONArray results = new JSONArray(Methods.readUrlContent(songsterrSearchUrl));

            for(int i = 0; i < Math.min(Integer.parseInt(maxNumberOfSongs), results.length()); i++) {
                JSONObject song = results.getJSONObject(i);
                int songsterrID = song.getInt("id");
                String artist = song.getJSONObject("artist").getString("name");
                String title = song.getString("title");
                String tabs_link = String.format("https://www.songsterr.com/a/wsa/%s %s-tab-s%d", artist, title, songsterrID);
                String lyrics = Methods.getLyrics(artist, title);
                String ytVidId = Methods.getYoutubeVideoID(artist, title);

                listOfSearchedSongs.add(new SearchedSong(i, title, artist, lyrics, tabs_link, ytVidId));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Methods.showMessage("Fatal Error", "Search could not complete!",
                    "See: " + e.getMessage());
            searchQuery = "";
            return "";
        }

        searchQuery = "";
        return "/songSearch/SearchResults?faces-redirect=true";
    }

}
