/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.PublicSong;
import edu.vt.EntityBeans.WishList;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.FacadeBeans.WishListFacade;
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

@Named("wishListController")
@SessionScoped
public class WishListController implements Serializable {

    @EJB
    private WishListFacade wishListFacade;

    @EJB
    private UserFacade userFacade;

    // List of object references of WishList objects
    private List<WishList> listOfWishLists = null;

    // selected = object reference of a selected WishList object
    private WishList selected;

    // Flag indicating if WishList data changed or not
    private Boolean WishListDataChanged;

    private String idOfVideoToPlay;

    public List<WishList> getListOfWishLists() {
        listOfWishLists = wishListFacade.findAll();

        List<WishList> toRemove = new ArrayList<WishList>();

        for(WishList song : listOfWishLists) {
            if(song.getUser_id() != userFacade.getUserID()) {
                toRemove.add(song);
            }
        }

        for(WishList toRem : toRemove) {
            listOfWishLists.remove(toRem);
        }

        return listOfWishLists;
    }

    public void setListOfWishLists(List<WishList> listOfWishLists) {
        this.listOfWishLists = listOfWishLists;
    }

    public WishList getSelected() {
        return selected;
    }

    public void setSelected(WishList selected) {
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
     Unselect a selected WishList object
     */
    public void unselect() {
        selected = null;
    }

    /*
     Cancel and redisplay the list
     */
    public String cancel() {
        // Unselect previously selected WishList object if any
        selected = null;
        return "/database/List?faces-redirect=true";
    }

    /*
     Prepare to create a new WishList
     */
    public void prepareCreate() {
        selected = new WishList();
    }

    /*
     Create a new WishList song for the database
     */
    public void create() {
        Methods.preserveMessages();

        selected.setLyrics(Methods.getLyrics(selected.getArtist(), selected.getTitle()));
        selected.setTabs_link(Methods.getTabsLink(selected.getArtist(), selected.getTitle()));
        selected.setYoutube_video_id(Methods.getYoutubeVideoID(selected.getArtist(), selected.getTitle()));
        selected.setUser_id(userFacade.getUserID());

        /* The object reference of the WishList to be created is stored in the instance variable 'selected' */
        persist(PersistAction.CREATE, "Song successfully added to wishlist!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfWishLists = null;          // Invalidate the listOfWishLists to trigger re-query.
            WishListDataChanged = true;
        }
    }

    /*
     Update the selected WishList in the database
     */
    public void update() {
        Methods.preserveMessages();

        /* The object reference of the WishList to be created is stored in the instance variable 'selected' */
        persist(PersistAction.UPDATE, "Song successfully updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfWishLists = null;      // Invalidate the listOfWishLists to trigger re-query.
            WishListDataChanged = true;
        }
    }

    /*
     Delete the selected WishList from the database
     */
    public void destroy() {
        destroy(false);
    }

    public void destroy(boolean display_message) {
        Methods.preserveMessages();

        /* The object reference of the WishList to be created is stored in the instance variable 'selected' */
        persist(PersistAction.DELETE, display_message ? "Song was successfully removed from wishlist!" : "");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfWishLists = null;          // Invalidate the listOfWishLists to trigger re-query.
            WishListDataChanged = true;
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
                    wishListFacade.edit(selected);
                } else {
                    /* Perform a Delete operation. */
                    wishListFacade.remove(selected);
                }
                if(successMessage.length() != 0) {
                    JsfUtil.addSuccessMessage(successMessage);
                }
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
