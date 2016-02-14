package de.uniluebeck.compilatex;

import de.uniluebeck.compilatex.dto.JobDTO;
import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.entity.Job;
import de.uniluebeck.compilatex.entity.JobFile;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class PersistenceManager {

    @PersistenceUnit(unitName = "compilatexPU")
    private final EntityManagerFactory emf;

    @PersistenceContext(unitName = "compilatexPU")
    private EntityManager entityManager;

    /**
     * instanticiate class and entity manager factory
     */
    public PersistenceManager() {
        emf = Persistence.createEntityManagerFactory("compilatexPU");
    }
    
    /**
     * create instance of entity manager
     * @return instanciated entity manager
     */
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
    
    /**
     * persist job dto in db (actually using merge operation)
     * @param dto job dto to persist
     * @return dto persisted and refreshed dto, now also containing the id
     */
    public JobDTO save(JobDTO dto) {
        return save(dto.toEntity()).toDTO();
    }

    /**
     * persist job entity in db (actually using merge operation)
     * @param entity job entity to persist
     * @return entity persisted and refreshed entity, now also containing the id
     */
    private Job save(Job entity) {
        return update(entity);
    }
    
    /**
     * update job dto (merge)
     * @param dto that will be updated
     * @return updated job dto
     */
    public JobDTO update(JobDTO dto) {
        return update(dto.toEntity()).toDTO();
    }

    /**
     * update job entity (merge)
     * @param entity that will be updated
     * @return updated job entity
     */
    private Job update(Job entity) {
        entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
        return entity;
    }
    
    /**
     * refresh job entity
     * @param dto that will be refreshed
     * @return dto the refreshed dto
     */
    public JobDTO refresh(JobDTO dto) {
        return refresh(dto.toEntity()).toDTO();
    }
    
    /**
     * refresh job entity
     * @param entity that will be refreshed
     * @return entity the refreshed entity
     */
    private Job refresh(Job entity) {
        entityManager = getEntityManager();
        entityManager.refresh(entity);
        entityManager.close();
        return entity;
    }
    
    /**
     * delete job with given id
     * @param id id for the job
     */
    public void delete(Long id) {
        entityManager = getEntityManager();
        Job entity = entityManager.find(Job.class, id);
        if (entity != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
    }
    
    /**
     * find job by id
     * @param id id for the job
     * @return job entity with given id
     */
    public JobDTO find(Long id) {
        Job entity = findEntity(id);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find job by id
     * @param id id for the job
     * @return job entity with given id
     */
    private Job findEntity(Long id) {
        entityManager = getEntityManager();
        Job entity = entityManager.find(Job.class, id);
        entityManager.close();
        return entity;
    }

    /**
     * find job by directory name in db
     * @param directory name of the job's directory
     * @return job dto with given directory name
     */
    public JobDTO findByDirectoryName(String directory) {
        Job entity = findEntityByDirectoryName(directory);
        return (entity != null) ? entity.toDTO() : null;
    }

    /**
     * find job by directory name in db
     * @param directory name of the job's directory
     * @return job entity with given directory name
     */
    private Job findEntityByDirectoryName(String directory) {
        entityManager = getEntityManager();
        TypedQuery<Job> query = entityManager.createQuery("SELECT e FROM " + Job.class.getSimpleName() + " e WHERE e.directory = '" + directory + "'", Job.class);
        Job entity = query.getSingleResult();
        entityManager.close();
        return entity;
    }

    /**
     * 
     * 
     * NEEDED FOR TESTING ONLY
     * 
     */
    /**
     * find all job entries in db
     * @return list with all job dtos
     */
    public List<JobDTO> findAll() {
        List<JobDTO> dtos = new ArrayList<>();
        for (Job entity : findAllEntities()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }

    /**
     * find all job entries in db
     * @return list with all job entities
     */
    private List<Job> findAllEntities() {
        entityManager = getEntityManager();
        TypedQuery<Job> query = entityManager.createQuery("SELECT e FROM " + Job.class.getSimpleName() + " e", Job.class);
        List<Job> entities = query.getResultList();
        entityManager.close();
        return entities;
    }
    
    /**
     * find all job file entries in db
     * @return list with all job file dtos
     */
    public List<JobDTO> findAllFiles() {
        List<JobDTO> dtos = new ArrayList<>();
        for (Job entity : findAllEntities()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }
    
    /**
     * find all job file entries in db
     * @return list with all job file entities
     */
    public List<JobFile> findAllFileEntities() {
        entityManager = getEntityManager();
        TypedQuery<JobFile> query = entityManager.createQuery("SELECT e FROM " + JobFile.class.getSimpleName() + " e", JobFile.class);
        List<JobFile> entities = query.getResultList();
        entityManager.close();
        return entities;
    }
}