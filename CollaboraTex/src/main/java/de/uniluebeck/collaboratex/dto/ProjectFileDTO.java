/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.dto;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import de.uniluebeck.collaboratex.entity.ProjectFile;
import de.uniluebeck.collaboratex.jackson.KeySerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class ProjectFileDTO {

    @JsonSerialize(using = KeySerializer.class)
    Key key;
    BlobKey blobKey;
    String name;
    String contentType;
    long size;
    long lastChanged;
    boolean mainTex;

    public ProjectFileDTO() {
        name = "";
        contentType = "";
        size = 0;
        lastChanged = System.currentTimeMillis();
        mainTex = false;
    }
    
    public ProjectFileDTO(BlobKey blobKey, String name, String contentType, long size, long lastChanged, boolean isMainTex) {
        this.blobKey = blobKey;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.lastChanged = lastChanged;
        this.mainTex = isMainTex;
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
    
    public BlobKey getBlobKey() {
        return blobKey;
    }

    public void setBlobKey(BlobKey blobKey) {
        this.blobKey = blobKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(long lastChanged) {
        this.lastChanged = lastChanged;
    }

    public boolean isMainTex() {
        return mainTex;
    }

    public void setMainTex(boolean mainTex) {
        this.mainTex = mainTex;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(blobKey).append(name).append(contentType).append(size).append(lastChanged).append(mainTex).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectFileDTO)) {
            return false;
        }

        ProjectFileDTO dto = (ProjectFileDTO) obj;
        return new EqualsBuilder().append(blobKey, dto.blobKey).append(name, dto.name).append(contentType, dto.contentType).append(size, dto.size).append(lastChanged, dto.lastChanged).append(mainTex, dto.mainTex).isEquals();
    }

    @Override
    public String toString() {
        return "ProjectFileDTO{" + "key=" + key + ", blobKey=" + blobKey + ", name=" + name + ", contentType=" + contentType + ", size=" + size +  ", lastChanged=" + lastChanged + ", isMainTex=" + mainTex + '}';
    }

    public ProjectFile toEntity() {
        ProjectFile entity = new ProjectFile();
        entity.setKey(getKey());
        entity.setBlobKey(getBlobKey());
        entity.setName(getName());
        entity.setContentType(getContentType());
        entity.setSize(getSize());
        entity.setLastChanged(getLastChanged());
        entity.setMainTex(isMainTex());
        return entity;
    }
}
