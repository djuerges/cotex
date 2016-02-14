/*
 * To change this license header, choose License Headers in ProjectFile Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.collaboratex.dao;

import de.uniluebeck.collaboratex.PersistenceManager;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.entity.ProjectFile;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class ProjectFileDAO implements DAO<ProjectFileDTO> {

    /**
     * create instance of persistance manager
     */
    static PersistenceManager pm = new PersistenceManager();
    
    /**
     * find project file by id
     *
     * @param id id for the project
     * @return project file entity with given id
     */
    @Override
    public ProjectFileDTO findById(Long id) {
        ProjectFile entity = findEntityById(id);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find project file by id
     *
     * @param id id for the project
     * @return project file entity with given id
     */
    private ProjectFile findEntityById(Long id) {
        EntityManager entityManager = pm.getEntityManager();
        ProjectFile entity = entityManager.find(ProjectFile.class, id);
        entityManager.close();
        return entity;
    }

    /**
     * find all project file entries in db
     *
     * @return list with all project file dtos
     */
    @Override
    public List<ProjectFileDTO> findAll() {
        List<ProjectFileDTO> dtos = new ArrayList<>();
        for (ProjectFile entity : findAllEntities()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }

    /**
     * find all project file entries in db
     *
     * @return list with all project file entities
     */
    private List<ProjectFile> findAllEntities() {
        EntityManager entityManager = pm.getEntityManager();
        TypedQuery<ProjectFile> query = entityManager.createQuery("SELECT e FROM " + ProjectFile.class.getSimpleName() + " e", ProjectFile.class);
        List<ProjectFile> entities = query.getResultList();
        entityManager.close();
        return entities;
    }

    /**
     * persist project file dto in db (actually using merge operation)
     *
     * @param dto project file dto to persist
     * @return dto persisted and refreshed dto, now also containing the id
     */
    @Override
    public ProjectFileDTO save(ProjectFileDTO dto) {
        return save(dto.toEntity()).toDTO();
    }

    /**
     * persist project file entity in db (actually using merge operation)
     *
     * @param entity project file entity to persist
     * @return entity persisted and refreshed entity, now also containing the
     * id
     */
    private ProjectFile save(ProjectFile entity) {
        return update(entity);
    }

    /**
     * update project file dto (merge)
     *
     * @param dto that will be updated
     * @return updated project file dto
     */
    @Override
    public ProjectFileDTO update(ProjectFileDTO dto) {
        return update(dto.toEntity()).toDTO();
    }
    
    /**
     * update project file entity (merge)
     *
     * @param entity that will be updated
     * @return updated project file entity
     */
    private ProjectFile update(ProjectFile entity) {
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
     * delete project file with given id
     *
     * @param id id for the project
     */
    @Override
    public void delete(Long id) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = pm.getEntityManager();
            ProjectFile entity = entityManager.find(ProjectFile.class, id);

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
     * @param name name of the project file
     * @return project file dto with given project file name
     */
    public ProjectFileDTO findByName(String name) {
        ProjectFile entity = findEntityByName(name);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find project by name in db
     *
     * @param name name of the project
     * @return project file entity with given project file name
     */
    private ProjectFile findEntityByName(String name) {
        EntityManager entityManager = pm.getEntityManager();
        TypedQuery<ProjectFile> query = entityManager.createQuery("SELECT e FROM " + ProjectFile.class.getSimpleName() + " e WHERE e.name = '" + name + "'", ProjectFile.class);
        ProjectFile entity = query.getSingleResult();
        entityManager.close();
        return entity;
    }
}
