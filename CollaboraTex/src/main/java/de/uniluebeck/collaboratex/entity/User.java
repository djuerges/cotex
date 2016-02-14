/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.entity;

import com.google.appengine.api.datastore.Key;
import de.uniluebeck.collaboratex.dto.UserDTO;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private String username;
    private Set<Key> ownedProjects;
    private Set<Key> accessibleProjects;
    
    public User() {
        username = "";
        this.ownedProjects = new HashSet<>();
        this.accessibleProjects = new HashSet<>();
    }

    public User(String username, Set<Key> ownedProjects, Set<Key> accessibleProjects) {
        this.username = username;
        this.ownedProjects = ownedProjects;
        this.accessibleProjects = accessibleProjects;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
    
    public Long getId() {
        return key.getId();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Set<Key> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<Key> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }
    
    public Set<Key> getAccessibleProjects() {
        return accessibleProjects;
    }

    public void setAccessibleProjects(Set<Key> accessibleProjects) {
        this.accessibleProjects = accessibleProjects;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(username).append(ownedProjects).append(accessibleProjects).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        User entity = (User) obj;
        return new EqualsBuilder().append(username, entity.username).append(ownedProjects, entity.ownedProjects).append(accessibleProjects, entity.accessibleProjects).isEquals();
    }

    @Override
    public String toString() {
        return "User{" + "key=" + key + ", username=" + username + ", ownedProjects=" + ownedProjects  + ", accessibleProjects=" + accessibleProjects + '}';
    }

    public UserDTO toDTO() {
        UserDTO dto = new UserDTO();
        dto.setKey(getKey());
        dto.setUsername(getUsername());
        dto.setOwnedProjects(getOwnedProjects());
        dto.setAccessibleProjects(getAccessibleProjects());
        return dto;
    }
}
