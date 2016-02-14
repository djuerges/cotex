/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.entity;

import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.dto.JobDTO;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String directory;

    @OneToMany(orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<JobFile> files;

    public Job() {
        directory = "";
        files = new HashSet<>();
    }
    
    public Job(String directory, Set<JobFile> files) {
        this.directory = directory;
        this.files = files;
    }

    public Job(Long id, String directory, Set<JobFile> files) {
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

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Set<JobFile> getFiles() {
        return files;
    }
    
    public void addFile(JobFile file) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(file);
    }

    public void setFiles(Set<JobFile> files) {
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
        if (!(obj instanceof Job))
            return false;

        Job entity = (Job) obj;
        return new EqualsBuilder().append(directory, entity.directory).append(files, entity.files).isEquals();
    }

    @Override
    public String toString() {
        return "de.uniluebeck.compilatex.Jobs[ id=" + id + " ]";
    }

    public JobDTO toDTO() {
        JobDTO dto = new JobDTO();
        dto.setId(getId());
        dto.setDirectory(getDirectory());
        dto.setFiles(toDTO(getFiles()));
        return dto;
    }

    public Set<JobFileDTO> toDTO(Set<JobFile> entities) {
        Set<JobFileDTO> dtos = new HashSet<>();
        for (JobFile entity : getFiles()) {
            dtos.add(entity.toDTO());
        }
        return dtos;
    }
}
