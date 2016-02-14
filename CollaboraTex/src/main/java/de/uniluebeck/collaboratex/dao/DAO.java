/*
 * To change this license header, choose License Headers in GenericEntity Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.dao;

import de.uniluebeck.collaboratex.PersistenceManager;
import java.util.List;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public interface DAO<T> {
    
    public T findById(Long id);
    public List<T> findAll();
    public T save(T dto);
    public T update(T dto);
    public void delete(Long id);
}