/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.dto;

import de.uniluebeck.compilatex.entity.JobFile;
import de.uniluebeck.compilatex.entity.Job;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class JobDTO {

    Long id;
    String directory;
    Set<JobFileDTO> files;

    public JobDTO() {
        directory = "";
        files = new HashSet<>();
    }
    
    public JobDTO(String directory, Set<JobFileDTO> files) {
        this.directory = directory;
        this.files = files;
    }

    public JobDTO(Long id, String directory, Set<JobFileDTO> files) {
        this.id = id;
        this.directory = directory;
        this.files = files;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String name) {
        this.directory = name;
    }

    public Set<JobFileDTO> getFiles() {
        return files;
    }
    
    public void addFile(JobFileDTO fileDTO) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(fileDTO);
    }

    public void setFiles(Set<JobFileDTO> files) {
        this.files = files;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(directory).append(files).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof JobDTO))
            return false;

        JobDTO dto = (JobDTO) obj;
        return new EqualsBuilder().append(directory, dto.directory).append(files, dto.files).isEquals();
    }

    @Override
    public String toString() {
        return "JobDTO{" + "id=" + id + ", projectName=" + directory + ", files=" + files + '}';
    }

    public Job toEntity() {
        Job entity = new Job();
        entity.setId(getId());
        entity.setDirectory(getDirectory());
        entity.setFiles(toEntity(getFiles()));
        return entity;
    }
    
    public Set<JobFile> toEntity(Set<JobFileDTO> dtos){
        Set<JobFile> entities = new HashSet<>();
        for(JobFileDTO dto : dtos){
            entities.add(dto.toEntity());
        }
        return entities;
    }
}
