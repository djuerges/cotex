/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.entity;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
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
public class ProjectFile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private BlobKey blobKey;
    private String name;
    private String contentType;
    private long size;
    private long lastChanged;
    private boolean mainTex;

    public ProjectFile() {
        name = "";
        contentType = "";
        size = 0;
        lastChanged = System.currentTimeMillis();
        mainTex = false;
    }
    
    public ProjectFile(BlobKey blobKey, String name, String contentType, long size, long lastChanged, boolean isMainTex) {
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
        if (!(obj instanceof ProjectFile)) {
            return false;
        }

        ProjectFile entity = (ProjectFile) obj;
        return new EqualsBuilder().append(blobKey, entity.blobKey).append(name, entity.name).append(contentType, entity.contentType).append(size, entity.size).append(lastChanged, entity.lastChanged).append(mainTex, entity.mainTex).isEquals();
    }

    @Override
    public String toString() {
        return "ProjectFile{" + "key=" + key + ", blobKey=" + blobKey + ", name=" + name + ", contentType=" + contentType + ", size=" + size + ", lastChanged=" + lastChanged + ", isMainTex=" + mainTex + '}';
    }

    public ProjectFileDTO toDTO() {
        ProjectFileDTO dto = new ProjectFileDTO();
        dto.setKey(getKey());
        dto.setBlobKey(getBlobKey());
        dto.setName(getName());
        dto.setContentType(getContentType());
        dto.setSize(getSize());
        dto.setLastChanged(getLastChanged());
        dto.setMainTex(isMainTex());
        return dto;
    }
}
