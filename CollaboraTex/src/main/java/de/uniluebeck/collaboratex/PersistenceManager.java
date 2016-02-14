package de.uniluebeck.collaboratex;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class PersistenceManager {
    
    @PersistenceUnit(unitName = "collaboratexPU")
    private static EntityManagerFactory emf;

    /**
     * instanticiate class and entity manager factory
     */
    public PersistenceManager() {
        emf = Persistence.createEntityManagerFactory("collaboratexPU");
    }
    
    /**
     * create instance of entity manager
     * @return instanciated entity manager
     */
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}