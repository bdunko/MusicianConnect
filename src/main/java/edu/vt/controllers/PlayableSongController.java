/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.PlayableSong;
import edu.vt.EntityBeans.PublicSong;
import edu.vt.EntityBeans.WishList;
import edu.vt.FacadeBeans.PlayableSongFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.SongSearch.SearchedSong;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("playableSongController")
@SessionScoped
public class PlayableSongController implements Serializable {

    @EJB
    private PlayableSongFacade playableSongFacade;

    @EJB
    private UserFacade userFacade;

    // List of object references of PlayableSong objects
    private List<PlayableSong> listOfPlayableSongs = null;

    // selected = object reference of a selected PlayableSong object
    private PlayableSong selected;

    private String idOfVideoToPlay;

    // Flag indicating if PlayableSong data changed or not
    private Boolean playableSongDataChanged;

    public List<PlayableSong> getListOfPlayableSongs() {
        listOfPlayableSongs = playableSongFacade.findAll();

        List<PlayableSong> toRemove = new ArrayList<PlayableSong>();

        for(PlayableSong song : listOfPlayableSongs) {
            if(song.getUser_id() != userFacade.getUserID()) {
                toRemove.add(song);
            }
        }

        for(PlayableSong toRem : toRemove) {
            listOfPlayableSongs.remove(toRem);
        }

        return listOfPlayableSongs;
    }

    public void setListOfPlayableSongs(List<PlayableSong> listOfPlayableSongs) {
        this.listOfPlayableSongs = listOfPlayableSongs;
    }

    public PlayableSong getSelected() {
        return selected;
    }

    public void setSelected(PlayableSong selected) {
        this.selected = selected;
    }

    public String getIdOfVideoToPlay() {
        return idOfVideoToPlay;
    }

    public void setIdOfVideoToPlay(String idOfVideoToPlay) {
        this.idOfVideoToPlay = idOfVideoToPlay;
    }

    /*
    ================
    Instance Methods
    ================
     */

    /*
     Unselect a selected PlayableSong object
     */
    public void unselect() {
        selected = null;
    }

    /*
     Cancel and redisplay the list
     */
    public String cancel() {
        // Unselect previously selected PlayableSong object if any
        selected = null;
        return "/database/List?faces-redirect=true";
    }

    /*
     Prepare to create a new PlayableSong
     */
    public void prepareCreate() {
        selected = new PlayableSong();
    }

    /*
     Create a new PlayableSong for the database
     */
    public void create() {
        Methods.preserveMessages();

        selected.setLyrics(Methods.getLyrics(selected.getArtist(), selected.getTitle()));
        selected.setTabs_link(Methods.getTabsLink(selected.getArtist(), selected.getTitle()));
        selected.setYoutube_video_id(Methods.getYoutubeVideoID(selected.getArtist(), selected.getTitle()));
        selected.setUser_id(userFacade.getUserID());

        /* The object reference of the PlayableSong to be created is stored in the instance variable 'selected' */
        persist(PersistAction.CREATE, "Song successfully added to playable songs!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfPlayableSongs = null;          // Invalidate the listOfPlayableSongs to trigger re-query.
            playableSongDataChanged = true;
        }
    }

    /*
     Update the selected PlayableSong in the database
     */
    public void update() {
        Methods.preserveMessages();

        /* The object reference of the PlayableSong to be created is stored in the instance variable 'selected' */
        persist(PersistAction.UPDATE, "Song successfully updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfPlayableSongs = null;      // Invalidate the listOfPlayableSongs to trigger re-query.
            playableSongDataChanged = true;
        }
    }

    /*
     Delete the selected PlayableSong from the database
     */
    public void destroy() {
        Methods.preserveMessages();

        /* The object reference of the PlayableSong to be created is stored in the instance variable 'selected' */
        persist(PersistAction.DELETE, "Song successfully removed from playable songs!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfPlayableSongs = null;          // Invalidate the listOfPlayableSongs to trigger re-query.
            playableSongDataChanged = true;
        }
    }

    //Adds a song from the PublicSong database to the Wishlist database
    public void addPublicSong(PublicSong song) {
        prepareCreate();
        selected.setUser_id(userFacade.getUserID());
        selected.setTitle(song.getTitle());
        selected.setArtist(song.getArtist());
        selected.setGenre(song.getGenre());
        selected.setDuration(song.getDuration());
        selected.setLyrics(song.getLyrics());
        selected.setTabs_link(song.getTabs_link());
        selected.setYoutube_video_id(song.getYoutube_video_id());
        create();
    }

    //Adds a song from the song search to the Wishlist database
    public void addSearchSong(SearchedSong song) {
        prepareCreate();
        selected.setUser_id(userFacade.getUserID());
        selected.setTitle(song.getTitle());
        selected.setArtist(song.getArtist());
        selected.setGenre("Unknown!");
        selected.setDuration("Unknown!");
        selected.setLyrics(song.getLyrics());
        selected.setTabs_link(song.getTabs_link());
        selected.setYoutube_video_id(song.getYoutube_video_id());
        create();
    }

    //Adds a song from the song search to the Wishlist database
    public void addWishlistSong(WishList song) {
        prepareCreate();
        selected.setUser_id(userFacade.getUserID());
        selected.setTitle(song.getTitle());
        selected.setArtist(song.getArtist());
        selected.setGenre("Unknown!");
        selected.setDuration("Unknown!");
        selected.setLyrics(song.getLyrics());
        selected.setTabs_link(song.getTabs_link());
        selected.setYoutube_video_id(song.getYoutube_video_id());
        create();
    }

    /*
     Perform CRUD operations on the database
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /* Perform a Create or Edit operations. */
                    playableSongFacade.edit(selected);
                } else {
                    /* Perform a Delete operation. */
                    playableSongFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred");
            }
        }
    }

}
