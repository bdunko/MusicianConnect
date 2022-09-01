/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Friend;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FriendFacade extends AbstractFacade<Friend> {

    @PersistenceContext(unitName = "MusicianConnect-Team5PU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public FriendFacade() {
        super(Friend.class);
    }

    public List<Friend> friendByIds(Integer myIDQ, Integer userIDQ) {
        return getEntityManager().createQuery(
                        "SELECT c FROM Friend c WHERE c.my_id = :myIDQ AND c.user_id = :userIDQ")
                .setParameter("myIDQ", myIDQ)
                .setParameter("userIDQ", userIDQ)
                .getResultList();
    }

}
