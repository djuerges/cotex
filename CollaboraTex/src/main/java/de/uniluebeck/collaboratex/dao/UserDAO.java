/*
 * To change this license header, choose License Headers in User Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.dao;

import de.uniluebeck.collaboratex.PersistenceManager;
import de.uniluebeck.collaboratex.dto.UserDTO;
import de.uniluebeck.collaboratex.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class UserDAO implements DAO<UserDTO> {

    /**
     * create instance of persistance manager
     */
    static PersistenceManager pm = new PersistenceManager();
    
    /**
     * find user by id
     *
     * @param id id for the user
     * @return user entity with given id
     */
    @Override
    public UserDTO findById(Long id) {
        User entity = findEntityById(id);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find user by id
     *
     * @param id id for the user
     * @return user entity with given id
     */
    private User findEntityById(Long id) {
        EntityManager entityManager = pm.getEntityManager();
        User entity = entityManager.find(User.class, id);
        entityManager.close();
        return entity;
    }

    /**
     * find all user entries in db
     *
     * @return list with all user dtos
     */
    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> dtos = new ArrayList<>();
        for (User entity : findAllEntities()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }

    /**
     * find all user entries in db
     *
     * @return list with all user entities
     */
    private List<User> findAllEntities() {
        EntityManager entityManager = pm.getEntityManager();
        TypedQuery<User> query = entityManager.createQuery("SELECT e FROM " + User.class.getSimpleName() + " e", User.class);
        List<User> entities = query.getResultList();
        entityManager.close();
        return entities;
    }

    /**
     * persist user dto in db (actually using merge operation)
     *
     * @param dto user dto to persist
     * @return dto persisted and refreshed dto, now also containing the id
     */
    @Override
    public UserDTO save(UserDTO dto) {
        return save(dto.toEntity()).toDTO();
    }

    /**
     * persist user entity in db (actually using merge operation)
     *
     * @param entity user entity to persist
     * @return entity persisted and refreshed entity, now also containing the
     * id
     */
    private User save(User entity) {
        return update(entity);
    }

    /**
     * update user dto (merge)
     *
     * @param dto that will be updated
     * @return updated user dto
     */
    @Override
    public UserDTO update(UserDTO dto) {
        return update(dto.toEntity()).toDTO();
    }

    /**
     * update user entity (merge)
     *
     * @param entity that will be updated
     * @return updated user entity
     */
    private User update(User entity) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = pm.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entity = entityManager.merge(entity);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
        
        return entity;
    }

    /**
     * delete user with given id
     *
     * @param id id for the user
     */
    @Override
    public void delete(Long id) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = pm.getEntityManager();
            User entity = entityManager.find(User.class, id);

            if (entity != null) {
                tx = entityManager.getTransaction();
                tx.begin();
                entityManager.remove(entity);
                tx.commit();
            }
            entityManager.close();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    /**
     * find user by name in db
     *
     * @param user name of the user
     * @return user dto with given user name
     */
    public UserDTO findByUsername(String user) {
        User entity = findEntityByUsername(user);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find user by name in db
     *
     * @param user name of the user
     * @return user entity with given user name
     */
    private User findEntityByUsername(String user) {
        EntityManager entityManager = pm.getEntityManager();
        TypedQuery<User> query = entityManager.createQuery("SELECT e FROM " + User.class.getSimpleName() + " e WHERE e.username = '" + user + "'", User.class);
        User entity = query.getSingleResult();
        entityManager.close();
        return entity;
    }
}