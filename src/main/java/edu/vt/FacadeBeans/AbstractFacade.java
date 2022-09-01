/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.FacadeBeans;

import java.util.List;
import javax.persistence.EntityManager;

// AbstractFacade is an abstract facade class providing a generic interface to the Entity Manager.
// Parameter T refers to the Class Type.
public abstract class AbstractFacade<T> {

    // An instance variable pointing to an entity class of type T
    private Class<T> entityClass;

    /*
    The following concrete facade classes inherit from this AbstractFacade class:

        - UserFacade                for User entity class               representing database table User
        - UserPhotoFacade           for UserPhoto entity class          representing database table UserPhoto
        - UserQuestionnaireFacade   for UserQuestionnaire entity class  representing database table UserQuestionnaire

     Each concrete facade class calls the following constructor method by passing entity
     class type T as a parameter. A concrete class provides the actual implementation.
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /*
    Declare the getEntityManager() abstract method as protected.
    The 'protected' keyword is an access modifier making the method
    accessible only in the same package and subclasses.
     */
    protected abstract EntityManager getEntityManager();

    // Stores the newly Created entity object into the database.
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    // Stores the Edited entity object into the database.
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    // Deletes (Removes) entity object from the database.
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    // Finds an entity object in the database by using its Primary Key (id) and returns it.
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    // Returns a list of object references of all of the entity objects found in the database.
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    // Returns a list of object references of all of the entity objects found in a range in the database.
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    // Obtains and returns the total number of entity objects in the database.
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
