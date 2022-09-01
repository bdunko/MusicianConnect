/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.PublicSong;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PublicSongFacade extends AbstractFacade<PublicSong> {

    @PersistenceContext(unitName = "MusicianConnect-Team5PU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public PublicSongFacade() {
        super(PublicSong.class);
    }

}
