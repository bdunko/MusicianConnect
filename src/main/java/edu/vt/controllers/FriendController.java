/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Friend;
import edu.vt.EntityBeans.PlayableSong;
import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.FriendFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.FacadeBeans.UserPhotoFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("friendController")
@SessionScoped
public class FriendController implements Serializable {
    @Inject
    private UserController userController;

    @EJB
    private FriendFacade friendFacade;

    @EJB
    private UserFacade userFacade;

    @EJB
    private UserPhotoFacade userPhotoFacade;

    // List of object references of Friend objects
    private List<Friend> listOfFriends = null;

    // selected = object reference of a selected Friend object
    private Friend selected;

    private String connectUsername;

    // Flag indicating if Friend data changed or not
    private Boolean friendDataChanged;

    public List<Friend> getListOfFriends() {
        listOfFriends = friendFacade.findAll();

        List<Friend> toRemove = new ArrayList<Friend>();

        for(Friend friend : listOfFriends) {
            if(friend.getMy_id() != userFacade.getUserID() && friend.getUser_id() != userFacade.getUserID()) {
                toRemove.add(friend);
            }
            else {
                if(userController.getSelected().getId().equals(friend.getUser_id())) {
                    friend.setActive(1);
                }
                else {
                    friend.setActive(0);
                }
            }
        }

        for(Friend toRem : toRemove) {
            listOfFriends.remove(toRem);
        }

        return listOfFriends;
    }

    public void setListOfFriends(List<Friend> listOfFriends) {
        this.listOfFriends = listOfFriends;
    }

    public Friend getSelected() {
        return selected;
    }

    public void setSelected(Friend selected) {
        this.selected = selected;
    }

    public String getConnectUsername() {
        return connectUsername;
    }

    public void setConnectUsername(String connectUsername) {
        this.connectUsername = connectUsername;
    }

    /*
    ================
    Instance Methods
    ================
     */

    /*
     Unselect a selected Friend object
     */
    public void unselect() {
        selected = null;
    }

    /*
     Cancel and redisplay the list
     */
    public String cancel() {
        // Unselect previously selected Friend object if any
        selected = null;
        return "/database/List?faces-redirect=true";
    }

    /*
     Prepare to create a new Friend
     */
    public void prepareCreate() {
        selected = new Friend();
    }

    /*
     Create a new Friend for the database
     */
    public void create() {
        Methods.preserveMessages();

        /* The object reference of the Friend to be created is stored in the instance variable 'selected' */
        persist(PersistAction.CREATE, "Connection was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfFriends = null;          // Invalidate the listOfFriends to trigger re-query.
            friendDataChanged = true;
        }
    }

    /*
     Update the selected Friend in the database
     */
    public void update() {
        Methods.preserveMessages();

        /* The object reference of the Friend to be created is stored in the instance variable 'selected' */
        persist(PersistAction.UPDATE, "Connection was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfFriends = null;      // Invalidate the listOfFriends to trigger re-query.
            friendDataChanged = true;
        }
    }

    /*
     Delete the selected friend from the database
     */
    public void destroy() {
        Methods.preserveMessages();

        /* The object reference of the friend to be created is stored in the instance variable 'selected' */
        persist(PersistAction.DELETE, "Connection was Successfully Removed!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfFriends = null;          // Invalidate the listOfFriends to trigger re-query.
            friendDataChanged = true;
        }
    }

    public void sendRequest(User toFriend) {
        User currentUser = userController.getLoggedInUser();
        if(toFriend != null) {
            if(!this.requestSent(toFriend)) {
                prepareCreate();
                selected.setMy_id(currentUser.getId());
                selected.setMy_username(currentUser.getUsername());
                selected.setMy_first_name(currentUser.getFirstName());
                selected.setMy_last_name(currentUser.getLastName());
                selected.setUsername(toFriend.getUsername());
                selected.setUser_id(toFriend.getId());
                selected.setFirst_name(toFriend.getFirstName());
                selected.setLast_name(toFriend.getLastName());
                selected.setIs_friend(0);
                selected.setActive(0);
                create();
            }
            else {
                JsfUtil.addSuccessMessage("Connection has already been initialized.");
            }
        }
        else {
            JsfUtil.addSuccessMessage("User does not exist in the database.");
        }
    }

    public void sendRequest() {
        User currentUser = userController.getSelected();
        User friendUser = userFacade.findByUsername(connectUsername);
        if(friendUser != null) {
            if(!this.requestSent(friendUser)) {
                prepareCreate();
                selected.setMy_id(currentUser.getId());
                selected.setMy_username(currentUser.getUsername());
                selected.setMy_first_name(currentUser.getFirstName());
                selected.setMy_last_name(currentUser.getLastName());
                selected.setUsername(connectUsername);
                selected.setUser_id(friendUser.getId());
                selected.setFirst_name(friendUser.getFirstName());
                selected.setLast_name(friendUser.getLastName());
                selected.setIs_friend(0);
                selected.setActive(0);
                create();
            }
            else {
                JsfUtil.addSuccessMessage("Connection has already been initialized.");
            }
        }
        else {
            JsfUtil.addSuccessMessage("User does not exist in the database.");
        }
    }

    public void acceptRequest() {
        if(!userController.getSelected().getUsername().equals(selected.getMy_username())) {
            if(selected.getIs_friend() == 0) {
                selected.setIs_friend(1);
                update();
            }
            else {
                JsfUtil.addSuccessMessage("Connection has already been accepted.");
            }
        }
        else {
            JsfUtil.addSuccessMessage("Cannot accept your own request.");
        }
    }

    public boolean requestSent(User friendUser) {
        List<Friend> friendsList = friendFacade.friendByIds(userController.getSelected().getId(), friendUser.getId());
        List<Friend> altList = friendFacade.friendByIds(friendUser.getId(), userController.getSelected().getId());
        if(friendsList.size() != 0 || altList.size() != 0) {
            return true;
        }
        return false;
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
                    friendFacade.edit(selected);
                } else {
                    /* Perform a Delete operation. */
                    friendFacade.remove(selected);
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

    public String connectedString(Friend aFriend) {
        if(aFriend != null) {
            if(aFriend.getIs_friend() == 0) {
                return "Pending";
            }
            else {
                return "Connected";
            }
        }
        return "Pending";
    }

    public boolean canContact() {
        if(selected != null && selected.getIs_friend() == 1) {
            return false;
        }
        return true;
    }

    public String getExperience() {
        User toDisplay = getUserFromFriend();
        return toDisplay.getExperience();
    }

    public String getInstrumentsPlayed() {
        User toDisplay = getUserFromFriend();
        return toDisplay.getInstruments_played();
    }

    public String getFavoriteGenre() {
        User toDisplay = getUserFromFriend();
        return toDisplay.getFavorite_genre();
    }

    public User getUserFromFriend() {
        User toReturn;
        if(selected.getActive() == 0) {
            toReturn = userFacade.getUser(selected.getUser_id());
        }
        else {
            toReturn = userFacade.getUser(selected.getMy_id());
        }
        return toReturn;
    }
}
