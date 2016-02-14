/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.dto;

import com.google.appengine.api.datastore.Key;
import de.uniluebeck.collaboratex.entity.User;
import de.uniluebeck.collaboratex.jackson.KeySerializer;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class UserDTO {

    @JsonSerialize(using = KeySerializer.class)
    Key key;
    String username;
    Set<Key> ownedProjects;
    Set<Key> accessibleProjects;

    public UserDTO() {
        username = "";
        this.ownedProjects = new HashSet<>();
        this.accessibleProjects = new HashSet<>();
    }
    
    public UserDTO(String username, Set<Key> ownedProjects, Set<Key> accessibleProjects) {
        this.username = username;
        this.ownedProjects = ownedProjects;
        this.accessibleProjects = new HashSet<>();
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
        if (!(obj instanceof UserDTO)) {
            return false;
        }

        UserDTO dto = (UserDTO) obj;
        return new EqualsBuilder().append(username, dto.username).append(ownedProjects, dto.ownedProjects).append(accessibleProjects, dto.accessibleProjects).isEquals();
    }

    @Override
    public String toString() {
        return "UserDTO{" + "key=" + key + ", projectName=" + username + ", ownedProjects=" + ownedProjects + ", accessibleProjects=" + accessibleProjects + "}";
    }

    public User toEntity() {
        User entity = new User();
        entity.setKey(getKey());
        entity.setUsername(getUsername());
        entity.setOwnedProjects(getOwnedProjects());
        entity.setAccessibleProjects(getAccessibleProjects());
        return entity;
    }
}
