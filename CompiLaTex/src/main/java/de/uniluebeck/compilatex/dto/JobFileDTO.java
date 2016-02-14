/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.dto;

import de.uniluebeck.compilatex.entity.JobFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class JobFileDTO {

    Long id;
    String name;
    String parentFolder;
    long lastChanged;
    private boolean isMainTex;

    public JobFileDTO() {
        name = "";
        parentFolder = "";
        lastChanged = System.currentTimeMillis();
        isMainTex = false;
    }
    
    public JobFileDTO(String name, String parentFolder, long lastChanged, boolean isMainTex) {
        this.name = name;
        this.parentFolder = parentFolder;
        this.lastChanged = lastChanged;
        this.isMainTex = isMainTex;
    }

    public JobFileDTO(Long id, String name, String parentFolder, long lastChanged, boolean isMainTex) {
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

    public boolean isMainTex() {
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
        if (!(obj instanceof JobFileDTO)) {
            return false;
        }

        JobFileDTO dto = (JobFileDTO) obj;
        return new EqualsBuilder().append(name, dto.name).append(parentFolder, dto.parentFolder).append(lastChanged, dto.lastChanged).append(isMainTex, dto.isMainTex).isEquals();
    }

    @Override
    public String toString() {
        return "JobFileDTO{" + "id=" + id + ", name=" + name + ", parentFolder=" + parentFolder + ", lastChanged=" + lastChanged + ", isMainTex=" + isMainTex + '}';
    }

    public JobFile toEntity() {
        JobFile entity = new JobFile();
        entity.setId(getId());
        entity.setName(getName());
        entity.setParentFolder(getParentFolder());
        entity.setLastChanged(getLastChanged());
        entity.setIsMainTex(isMainTex());
        return entity;
    }
}
