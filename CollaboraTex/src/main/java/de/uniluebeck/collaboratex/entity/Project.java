/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.entity;

import com.google.appengine.api.datastore.Key;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private String name;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProjectFile> files;

    public Project() {
        name = "";
        files = new HashSet<>();
    }

    public Project(String name, Set<ProjectFile> files) {
        this.name = name;
        this.files = files;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProjectFile> getFiles() {
        if (files == null) {
            files = new HashSet<>();
        }
        return files;
    }
    
    public void addFiles(Set<ProjectFile> files) {
        for(ProjectFile file : files){
            addFile(file);
        }
    }

    public void addFile(ProjectFile file) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(file);
    }

    public void setFiles(Set<ProjectFile> files) {
        this.files = files;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(name).append(files).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Project)) {
            return false;
        }

        Project entity = (Project) obj;
        return new EqualsBuilder().append(name, entity.name).append(files, entity.files).isEquals();
    }

    @Override
    public String toString() {
        return "Project{" + "key=" + key + ", name=" + name + '}';
    }

    public ProjectDTO toDTO() {
        ProjectDTO dto = new ProjectDTO();
        dto.setKey(getKey());
        dto.setName(getName());
        dto.setFiles(toDTO(getFiles()));
        return dto;
    }

    public Set<ProjectFileDTO> toDTO(Set<ProjectFile> entities) {
        Set<ProjectFileDTO> dtos = new HashSet<>();
        for (ProjectFile entity : getFiles()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }
}
