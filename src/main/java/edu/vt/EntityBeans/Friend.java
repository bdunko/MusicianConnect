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
@Table(name = "Friend")

@NamedQueries({
        // This named query is used in SongFacade
        @NamedQuery(name = "Friend.findByUsername", query = "SELECT c FROM Friend c WHERE c.username = :username")
})

public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * CREATE TABLE Friend
     * (
     *     id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
     *     user_id INT UNSIGNED,
     *     username VARCHAR(32) NOT NULL,
     *     first_name VARCHAR(32) NOT NULL,
     *     last_name VARCHAR(32) NOT NULL,
     *     is_friend INT UNSIGNED,
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

    // Username of the friend
    // username VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "username")
    private String username;

    //First name of the friend
    //first_name VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "first_name")
    private String first_name;

    //Last name of the friend
    //last_name VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "last_name")
    private String last_name;

    //Boolean representing if the user is a friend (0 = not friend, 1 = friend)
    //is_friend INT UNSIGNED
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_friend")
    private Integer is_friend;

    //Id representing the user whose friend object is associated with it
    //my_id INT UNSIGNED
    @Basic(optional = false)
    @NotNull
    @Column(name = "my_id")
    private Integer my_id;

    // Username of the friend
    // username VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "my_username")
    private String my_username;

    //First name of the friend
    //first_name VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "my_first_name")
    private String my_first_name;

    //Last name of the friend
    //last_name VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "my_last_name")
    private String my_last_name;

    //Boolean representing if the user is a friend (0 = not friend, 1 = friend)
    //is_friend INT UNSIGNED
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Integer active;

    /* Constructors to create an instance of Friend */
    // Used in PrepareCreate method in FriendController
    public Friend() {
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

    public String getUsername() {
        if(active == 1) {
            return my_username;
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        if(active == 1) {
            return my_first_name;
        }
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        if(active == 1) {
            return my_last_name;
        }
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(Integer is_friend) {
        this.is_friend = is_friend;
    }

    public Integer getMy_id() {
        return my_id;
    }

    public void setMy_id(Integer my_id) {
        this.my_id = my_id;
    }

    public void setMy_username(String my_username) {
        this.my_username = my_username;
    }

    public void setMy_first_name(String my_first_name) {
        this.my_first_name = my_first_name;
    }

    public void setMy_last_name(String my_last_name) {
        this.my_last_name = my_last_name;
    }

    public String getMy_username() {
        return my_username;
    }

    public String getMy_first_name() {
        return my_first_name;
    }

    public String getMy_last_name() {
        return my_last_name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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
        if (!(object instanceof Friend)) {
            return false;
        }
        Friend other = (Friend) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

}
