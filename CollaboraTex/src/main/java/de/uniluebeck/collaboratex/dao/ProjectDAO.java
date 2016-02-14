/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.dao;

import de.uniluebeck.collaboratex.PersistenceManager;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import de.uniluebeck.collaboratex.entity.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class ProjectDAO implements DAO<ProjectDTO> {

    /**
     * create instance of persistance manager
     */
    static PersistenceManager pm = new PersistenceManager();

    /**
     * find project by id
     *
     * @param id id for the project
     * @return project entity with given id
     */
    @Override
    public ProjectDTO findById(Long id) {
        Project entity = findEntityById(id);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find project by id
     *
     * @param id id for the project
     * @return project entity with given id
     */
    private Project findEntityById(Long id) {
        EntityManager entityManager = pm.getEntityManager();
        Project entity = entityManager.find(Project.class, id);
        entityManager.close();
        return entity;
    }

    /**
     * find all project entries in db
     *
     * @return list with all project dtos
     */
    @Override
    public List<ProjectDTO> findAll() {
        List<ProjectDTO> dtos = new ArrayList<>();
        for (Project entity : findAllEntities()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }

    /**
     * find all project entries in db
     *
     * @return list with all project entities
     */
    private List<Project> findAllEntities() {
        EntityManager entityManager = pm.getEntityManager();
        TypedQuery<Project> query = entityManager.createQuery("SELECT e FROM " + Project.class.getSimpleName() + " e", Project.class);
        List<Project> entities = query.getResultList();
        entityManager.close();
        return entities;
    }

    /**
     * persist project dto in db (actually using merge operation)
     *
     * @param dto project dto to persist
     * @return dto persisted and refreshed dto, now also containing the id
     */
    @Override
    public ProjectDTO save(ProjectDTO dto) {
        return save(dto.toEntity()).toDTO();
    }

    /**
     * persist project entity in db (actually using merge operation)
     *
     * @param entity project entity to persist
     * @return entity persisted and refreshed entity, now also containing the id
     */
    private Project save(Project entity) {
        return update(entity);
    }

    /**
     * update project dto (merge)
     *
     * @param dto that will be updated
     * @return updated project dto
     */
    @Override
    public ProjectDTO update(ProjectDTO dto) {
        return update(dto.toEntity()).toDTO();
    }

    /**
     * update project entity (merge)
     *
     * @param entity that will be updated
     * @return updated project entity
     */
    private Project update(Project entity) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = pm.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entity = entityManager.merge(entity);
            tx.commit();
            entityManager.close();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }

        return entity;
    }

    /**
     * delete project with given id
     *
     * @param id id for the project
     */
    @Override
    public void delete(Long id) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = pm.getEntityManager();
            Project entity = entityManager.find(Project.class, id);

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
     * find project by name in db
     *
     * @param project name of the project
     * @return project dto with given project name
     */
    public ProjectDTO findByName(String project) {
        Project entity = findEntityByName(project);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find project by name in db
     *
     * @param project name of the project
     * @return project entity with given project name
     */
    private Project findEntityByName(String project) {
        EntityManager entityManager = pm.getEntityManager();
        TypedQuery<Project> query = entityManager.createQuery("SELECT e FROM " + Project.class.getSimpleName() + " e WHERE e.name = '" + project + "'", Project.class);
        Project entity = query.getSingleResult();
        entityManager.close();
        return entity;
    }
}
