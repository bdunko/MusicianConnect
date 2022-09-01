/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.PublicSong;
import edu.vt.FacadeBeans.PublicSongFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONObject;
import edu.vt.globals.Constants;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("publicSongController")
@SessionScoped
public class PublicSongController implements Serializable {

    @EJB
    private PublicSongFacade publicSongFacade;

    // List of object references of PublicSong objects
    private List<PublicSong> listOfPublicSongs = null;

    // selected = object reference of a selected PublicSong object
    private PublicSong selected;

    // Flag indicating if PublicSong data changed or not
    private Boolean publicSongDataChanged;

    private String idOfVideoToPlay;

    public List<PublicSong> getListOfPublicSongs() {
        listOfPublicSongs = publicSongFacade.findAll();

        return listOfPublicSongs;
    }

    /*
    ================
    Getters and Setters
    ================
     */

    public void setListOfPublicSongs(List<PublicSong> listOfPublicSongs) {
        this.listOfPublicSongs = listOfPublicSongs;
    }

    public PublicSong getSelected() {
        return selected;
    }

    public void setSelected(PublicSong selected) {
        this.selected = selected;
    }

    public String getIdOfVideoToPlay() {
        return idOfVideoToPlay;
    }

    public void setIdOfVideoToPlay(String idOfVideoToPlay) {
        this.idOfVideoToPlay = idOfVideoToPlay;
    }

    /*
         Unselect a selected PublicSong object
         */
    public void unselect() {
        selected = null;
    }

    /*
     Cancel and redisplay the list
     */
    public String cancel() {
        // Unselect previously selected PublicSong object if any
        selected = null;
        return "/database/List?faces-redirect=true";
    }

    /*
     Prepare to create a new PublicSong
     */
    public void prepareCreate() {
        selected = new PublicSong();
    }

    /*
     Create a new PublicSong for the database
     */
    public void create() {
        Methods.preserveMessages();

        selected.setLyrics(Methods.getLyrics(selected.getArtist(), selected.getTitle()));
        selected.setTabs_link(Methods.getTabsLink(selected.getArtist(), selected.getTitle()));
        selected.setYoutube_video_id(Methods.getYoutubeVideoID(selected.getArtist(), selected.getTitle()));

        /* The object reference of the PublicSong to be created is stored in the instance variable 'selected' */
        persist(PersistAction.CREATE, "Song successfully created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfPublicSongs = null;          // Invalidate the listOfPublicSongs to trigger re-query.
            publicSongDataChanged = true;
        }
    }

    /*
     Update the selected PublicSong in the database
     */
    public void update() {
        Methods.preserveMessages();

        /* The object reference of the PublicSong to be created is stored in the instance variable 'selected' */
        persist(PersistAction.UPDATE, "Song successfully updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfPublicSongs = null;      // Invalidate the listOfPublicSongs to trigger re-query.
            publicSongDataChanged = true;
        }
    }

    /*
     Delete the selected PublicSong from the database
     */
    public void destroy() {
        Methods.preserveMessages();

        /* The object reference of the PublicSong to be created is stored in the instance variable 'selected' */
        persist(PersistAction.DELETE, "Song successfully deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfPublicSongs = null;          // Invalidate the listOfPublicSongs to trigger re-query.
            publicSongDataChanged = true;
        }
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
                    publicSongFacade.edit(selected);
                } else {
                    /* Perform a Delete operation. */
                    publicSongFacade.remove(selected);
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
