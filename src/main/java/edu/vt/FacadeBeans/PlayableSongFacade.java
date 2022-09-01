/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.PlayableSong;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PlayableSongFacade extends AbstractFacade<PlayableSong> {

    @PersistenceContext(unitName = "MusicianConnect-Team5PU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public PlayableSongFacade() {
        super(PlayableSong.class);
    }

}
