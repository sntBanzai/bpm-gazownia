/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "path")
@NamedQueries({
    @NamedQuery(name = "Path.findAll", query = "SELECT p FROM Path p")})
public class Path implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @NotNull
    @Column(name = "directory")
    private boolean directory;
    @JoinColumn(name = "id_filesystem", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Filesystem filesystem;
    @JoinColumn(name = "id_parent_path", referencedColumnName = "id")
    @ManyToOne
    private Path parentPath;
    @JoinColumn(name = "id_path_content_current", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PathContent pathContent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "path")
    private Set<PathProperty> pathPropertyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "path")
    private Set<PathContent> pathContentList;

    public Path() {
    }

    public Path(Long id) {
        this.id = id;
    }

    public Path(Long id, String value, boolean directory) {
        this.id = id;
        this.value = value;
        this.directory = directory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public Filesystem getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(Filesystem filesystem) {
        this.filesystem = filesystem;
    }

    public Path getParentPath() {
        return parentPath;
    }

    public void setParentPath(Path parentPath) {
        this.parentPath = parentPath;
    }

    public PathContent getPathContent() {
        return pathContent;
    }

    public void setPathContent(PathContent pathContent) {
        this.pathContent = pathContent;
    }

    public Set<PathProperty> getPathPropertyList() {
        return pathPropertyList;
    }

    public void setPathPropertyList(Set<PathProperty> pathPropertyList) {
        this.pathPropertyList = pathPropertyList;
    }

    public Set<PathContent> getPathContentList() {
        return pathContentList;
    }

    public void setPathContentList(Set<PathContent> pathContentList) {
        this.pathContentList = pathContentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Path)) {
            return false;
        }
        Path other = (Path) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "[ id=" + id + " ]";
    }

}
