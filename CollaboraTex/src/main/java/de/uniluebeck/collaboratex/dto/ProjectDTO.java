/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.dto;

import com.google.appengine.api.datastore.Key;
import de.uniluebeck.collaboratex.entity.Project;
import de.uniluebeck.collaboratex.entity.ProjectFile;
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
public class ProjectDTO {

    @JsonSerialize(using = KeySerializer.class)
    Key key;
    String name;
    Set<ProjectFileDTO> files;

    public ProjectDTO() {
        name = "";
        files = new HashSet<>();
    }

    public ProjectDTO(String name, Set<ProjectFileDTO> files) {
        this.name = name;
        this.files = files;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProjectFileDTO> getFiles() {
        if (files == null) {
            files = new HashSet<>();
        }
        return files;
    }

    public void addFiles(Set<ProjectFileDTO> files) {
        for(ProjectFileDTO file : files){
            addFile(file);
        }
    }
    
    public void addFile(ProjectFileDTO file) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(file);
    }
    
    public void setFiles(Set<ProjectFileDTO> files) {
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
        if (!(obj instanceof ProjectDTO)) {
            return false;
        }

        ProjectDTO dto = (ProjectDTO) obj;
        return new EqualsBuilder().append(name, dto.name).append(files, dto.files).isEquals();
    }

    @Override
    public String toString() {
        return "ProjectDTO{" + "key=" + key + ", name=" + name + ", files=" + files + '}';
    }

    public Project toEntity() {
        Project entity = new Project();
        entity.setKey(getKey());
        entity.setName(getName());
        entity.setFiles(toEntity(getFiles()));
        return entity;
    }

    public Set<ProjectFile> toEntity(Set<ProjectFileDTO> dtos) {
        Set<ProjectFile> entities = new HashSet<>();
        for (ProjectFileDTO dto : dtos) {
            entities.add(dto.toEntity());
        }
        return entities;
    }
}
