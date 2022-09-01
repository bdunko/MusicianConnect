/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.security.MessageDigest;

@Entity

// Name of the database table represented
@Table(name = "Message")

//@NamedQueries({
//        // This named query is used in SongFacade
//        @NamedQuery(name = "Message.findByUsername", query = "SELECT c FROM Friend c WHERE c.username = :username")
//})

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * CREATE TABLE Message
     * (
     *     id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
     *     user_id INT UNSIGNED,
     *     message VARCHAR(4000) NOT NULL,
     *     FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
     * );
     */

    // Primary key for the entry
    // id INT UNSIGNED NOT NULL AUTO_INCREMENT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    //ID of the friend user
    //user_id INT UNSIGNED
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer user_id;

    // Post message
    // message VARCHAR(4000) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "message")
    private String message;

    /* Constructors to create an instance of Friend */
    // Used in PrepareCreate method in FriendController
    public Message() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /*
     Used to compare public song objects
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

}
