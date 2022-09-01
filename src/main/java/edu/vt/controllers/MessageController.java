/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Message;
import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.MessageFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("messageController")
@SessionScoped
public class MessageController implements Serializable {

    @EJB
    private MessageFacade messageFacade;

    @EJB
    private UserFacade userFacade;

    // List of object references of Message objects
    private List<Message> listOfMessages = null;

    // selected = object reference of a selected Message object
    private Message selected;

    // Flag indicating if Message data changed or not
    private Boolean messageDataChanged;

    public List<Message> getListOfMessages() {
        listOfMessages = messageFacade.findAll();

        return listOfMessages;
    }

    /*
    ================
    Getters and Setters
    ================
     */

    public void setListOfMessage(List<Message> listOfMessage) {
        this.listOfMessages = listOfMessage;
    }

    public Message getSelected() {
        return selected;
    }

    public void setSelected(Message selected) {
        this.selected = selected;
    }

    /*
         Unselect a selected Message object
         */
    public void unselect() {
        selected = null;
    }

    /*
     Cancel and redisplay the list
     */
    public String cancel() {
        // Unselect previously selected Message object if any
        selected = null;
        return "/messageBoard/List?faces-redirect=true";
    }

    public boolean isMessageFriendable(User current) {
        if(selected == null) {
            return false;
        }
        return !current.equals(userFacade.getUser(selected.getUser_id())); //can't friend yourself!
    }

    public boolean isMessageEditable(User current) {
        if(selected == null) {
            return false;
        }
        if(current.getUsername().equals("Administrator")) {
            return true;
        }
        return current.equals(userFacade.getUser(selected.getUser_id()));
    }

    /*
     Prepare to create a new Message
     */
    public void prepareCreate() {
        selected = new Message();
    }

    /*
     Create a new Message for the database
     */
    public void create() {
        Methods.preserveMessages();

        selected.setUser_id(userFacade.getUserID());

        /* The object reference of the Message to be created is stored in the instance variable 'selected' */
        persist(PersistAction.CREATE, "Message successfully posted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfMessages = null;          // Invalidate the listOfMessages to trigger re-query.
            messageDataChanged = true;
        }
    }

    /*
     Update the selected Message in the database
     */
    public void update() {
        Methods.preserveMessages();

        /* The object reference of the Message to be created is stored in the instance variable 'selected' */
        persist(PersistAction.UPDATE, "Message successfully updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfMessages = null;      // Invalidate the listOfMessages to trigger re-query.
            messageDataChanged = true;
        }
    }

    /*
     Delete the selected Message from the database
     */
    public void destroy() {
        Methods.preserveMessages();

        /* The object reference of the Message to be created is stored in the instance variable 'selected' */
        persist(PersistAction.DELETE, "Message successfully removed!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfMessages = null;          // Invalidate the listOfPublicSongs to trigger re-query.
            messageDataChanged = true;
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
                    messageFacade.edit(selected);
                } else {
                    /* Perform a Delete operation. */
                    messageFacade.remove(selected);
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
