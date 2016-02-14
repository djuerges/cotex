/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.entity;

import de.uniluebeck.compilatex.dto.JobFileDTO;
import java.io.Serializable;
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
public class JobFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    private String parentFolder;

    private long lastChanged;

    private boolean isMainTex;

    public JobFile() {
        name = "";
        parentFolder = "";
        lastChanged = System.currentTimeMillis();
        isMainTex = false;
    }
    
    public JobFile(String name, String parentFolder, long lastChanged, boolean isMainTex) {
        this.name = name;
        this.parentFolder = parentFolder;
        this.lastChanged = lastChanged;
        this.isMainTex = isMainTex;
    }

    public JobFile(Long id, String parentFolder, String name, long lastChanged, boolean isMainTex) {
        this.id = id;
        this.name = name;
        this.parentFolder = parentFolder;
        this.lastChanged = lastChanged;
        this.isMainTex = isMainTex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    public long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(long lastChanged) {
        this.lastChanged = lastChanged;
    }

    public boolean isIsMainTex() {
        return isMainTex;
    }

    public void setIsMainTex(boolean isMainTex) {
        this.isMainTex = isMainTex;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(name).append(lastChanged).append(isMainTex).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof JobFile)) {
            return false;
        }

        JobFile entity = (JobFile) obj;
        return new EqualsBuilder().append(name, entity.name).append(parentFolder, entity.parentFolder).append(lastChanged, entity.lastChanged).append(isMainTex, entity.isMainTex).isEquals();
    }

    @Override
    public String toString() {
        return "JobFile{" + "id=" + id + ", name=" + name + ", parentFolder=" + parentFolder + ", lastChanged=" + lastChanged + ", isMainTex=" + isMainTex + '}';
    }

    public JobFileDTO toDTO() {
        JobFileDTO dto = new JobFileDTO();
        dto.setId(getId());
        dto.setName(getName());
        dto.setParentFolder(getParentFolder());
        dto.setLastChanged(getLastChanged());
        dto.setIsMainTex(isIsMainTex());
        return dto;
    }
}
