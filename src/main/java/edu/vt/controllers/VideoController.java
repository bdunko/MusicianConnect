/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Video;
import edu.vt.EntityBeans.User;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.VideoFacade;
import edu.vt.globals.Methods;
import edu.vt.globals.Constants;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "VideoController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("videoController")

/*
The @SessionScoped annotation preserves the values of the VideoController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the videoController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
 */

public class VideoController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    PublicVideoFacade bean into the instance variable 'publicVideoFacade' after it is instantiated at runtime.
     */
    @EJB
    private VideoFacade videoFacade;

    // List of object references of Video objects
    private List<Video> listOfVideos = null;

    // selected = object reference of a selected Video object
    private Video selected;

    // Flag indicating if Video data changed or not
    private Boolean videoDataChanged;

    // searchItems = List of object references of Video objects found in the search
    private List<Video> searchItems = null;

    // searchField refers to searching video category
    private String searchField;

    // searchString contains the character string the user entered for searching the selected searchField
    private String searchString;

    // Search type, only 1
    private Integer searchType;

    // Search Query Variables (Q = Query)
    private String categoryQ;

    /*
    =========================
    Getter and Setter Methods
    =========================
    */
    public List<Video> getListOfVideos() {
        if (listOfVideos == null) {
            listOfVideos = videoFacade.findAll();
        }
        return listOfVideos;
    }

    public Video getSelected() {
        return selected;
    }

    public void setSelected(Video selected) {
        this.selected = selected;
    }

    public Boolean getVideoDataChanged() {
        return videoDataChanged;
    }

    public void setVideoDataChanged(Boolean videoDataChanged) {
        this.videoDataChanged = videoDataChanged;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getCategoryQ() {
        return categoryQ;
    }

    public void setCategoryQ(String categoryQ) {
        this.categoryQ = categoryQ;
    }

    /*
    ================
    Instance Methods
    ================
     */

    /*
     ****************************************
     *   Unselect Selected Video Object   *
     ****************************************
     */
    public void unselect() {
        selected = null;
    }

    /*
     *************************************
     *   Cancel and Display List.xhtml   *
     *************************************
     */
    public String cancel() {
        // Unselect previously selected Video object if any
        selected = null;
        return "/videos/List?faces-redirect=true";
    }

    /*
     ***************************************
     *   Prepare to Create a New PublicVideo   *
     ***************************************
     */
    public void prepareCreate() {
        /*
        Instantiate a new Video object and store its object reference into
        instance variable 'selected'. The Video class is defined in Video.java
         */
        selected = new Video();
    }

    public void create() {
        Methods.preserveMessages();

        /*
         The object reference of the video to be created is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(PersistAction.CREATE, "Video was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfVideos = null;     // Invalidate listOfVideos to trigger re-query.
            videoDataChanged = true;
        }
    }

    /*
     ***********************************************
     *   UPDATE Selected Video in the Database   *
     ***********************************************
     */
    public void update() {
        Methods.preserveMessages();
        /*
         The object reference of the Video to be updated is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(PersistAction.UPDATE, "Video was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfVideos = null; // Invalidate listOfVideos to trigger re-query.
            videoDataChanged = true;
        }
    }

    /*
     *************************************************
     *   DELETE Selected PublicVideo from the Database   *
     *************************************************
     */
    public void destroy() {
        Methods.preserveMessages();
        /*
         The object reference of the video to be deleted is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(PersistAction.DELETE, "Video was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfVideos = null;     // Invalidate list of Videos to trigger re-query.
            videoDataChanged = true;
        }
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     VideoFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    videoFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     VideoFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    videoFacade.remove(selected);
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

    /*
     ******************************************
     *   Display the Search Results JSF Page  *
     ******************************************
     */
    public String search() {

        // Unselect previously selected video if any before showing the search results
        selected = null;

        // Invalidate list of search items to trigger re-query.
        searchItems = null;

        return "/videos/DatabaseSearchResults?faces-redirect=true";
    }

    /*
     ****************************************************************************************************
     *   Return the list of object references of all those publicVideos that satisfy the search criteria   *
     ****************************************************************************************************
     */
    // This is the Getter method for the instance variable searchItems
    public List<Video> getSearchItems() {
        /*
        =============================================================================================
        You must construct and return the search results List "searchItems" ONLY IF the List is null.
        Any List provided to p:dataTable to display must be returned ONLY IF the List is null
        ===> in order for the column-sort to work. <===
        =============================================================================================
         */
        if (searchItems == null) {
            // Return the list of object references of all those Videos where
            // category contains the searchString entered by the user.
            searchItems = videoFacade.categoryQuery(searchString);
        }
        return searchItems;
    }
}
