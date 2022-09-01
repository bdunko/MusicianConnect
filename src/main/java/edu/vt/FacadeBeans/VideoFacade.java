/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Video;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless

public class VideoFacade extends AbstractFacade<Video> {
    /*
    ---------------------------------------------------------------------------------------------
    The EntityManager is an API that enables database CRUD (Create Read Update Delete) operations
    and complex database searches. An EntityManager instance is created to manage entities
    that are defined by a persistence unit. The @PersistenceContext annotation below associates
    the entityManager instance with the persistence unitName identified below.
    ---------------------------------------------------------------------------------------------
    */
    @PersistenceContext(unitName = "MusicianConnect-Team5PU")
    private EntityManager entityManager;

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
    */
    public VideoFacade() {
        super(Video.class);
    }

    /*
     *********************
     *   Other Methods   *
     *********************
     */
    // Returns the object reference of the PublicVideo object whose name is publicVideo_title
    public Video findByVideoTitle(String video_title) {
        /*
        The following @NamedQuery definition is given in Video.java entity class file:
        @NamedQuery(name = "Video.findByTitle", query = "SELECT c FROM Video c WHERE c.title = :title")
         */

        if (entityManager.createNamedQuery("Video.findByTitle")
                .setParameter("title", video_title)
                .getResultList().isEmpty()) {

            // Return null if no Video object exists by the name of video_title
            return null;

        } else {

            // Return the Object reference of the Video object whose title is video_title
            return (Video) (entityManager.createNamedQuery("Video.findByTitle")
                    .setParameter("title", video_title)
                    .getSingleResult());
        }
    }

    /*
    ***********************************************************************************
    *   Java Persistence API (JPA) Query Formulation for Searching a MySQL Database   *
    ***********************************************************************************
    By default, MySQL does not distinguish between upper and lower case letters in searches.
    Therefore, searches based on the queries below are all case insensitive by default.

    The LIKE Expression
        SELECT c FROM PublicVideo c WHERE c.title LIKE :'in%'       All publicVideos whose title begins with "in"
        SELECT c FROM PublicVideo c WHERE c.title LIKE :'%tan'      All publicVideos whose title ends with "tan"
        SELECT c FROM PublicVideo c WHERE c.title LIKE :'%ge%'      All publicVideos whose title contains "ge"

    The LIKE expression uses wildcard character % to search for strings that match the wildcard pattern.

    ================================
    EntityManager Method createQuery
    ================================
    Query createQuery(String qlString)
        Create an instance of Query for executing a Java Persistence (JPA) query language statement.
    Parameter:
        qlString - a Java Persistence query string, e.g., "SELECT c FROM PublicVideo c WHERE c.title LIKE :searchString"
    Returns:
        the object reference of the newly created Query object

    =========================
    Query Method setParameter
    =========================
    Query setParameter(String name, Object value)
        Bind an argument value to a named parameter
    Parameters:
        name - parameter name (e.g., "searchString")
        value - parameter value (e.g., the searchString parameter that contains the search string the user entered for searching)
    Returns:
        the same object reference of the newly created Query object

    ==========================
    Query Method getResultList
    ==========================
    List getResultList()
        Execute a SELECT query and return the query results as an untyped List
    Returns:
        the object reference of the newly created List containing the search results
    */

    /*
     ***************************
     *   Search Query: CATEGORY   *
     ***************************
     */
    /*
    -----------------------------
    Search Category: PublicVideo CATEGORY
    -----------------------------
     */
    // Searches UsersVideosDB for publicVideos where PublicVideo category contains the searchString entered by the user.
    public List<Video> categoryQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the Video category
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT c FROM Video c WHERE c.category LIKE :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
    }
}
