/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs.entity;

import java.io.Serializable;
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

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "path_property")
@NamedQueries({
    @NamedQuery(name = "PathProperty.findAll", query = "SELECT p FROM PathProperty p")})
public class PathProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_path", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Path path;
    @JoinColumn(name = "id_path_property_type", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PathPropertyType pathPropertyType;
    @JoinColumn(name = "id_path_property_value_current", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PathPropertyValue pathPropertyValue;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pathProperty")
    private Set<PathPropertyValue> pathPropertyValueList;

    public PathProperty() {
    }

    public PathProperty(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public PathPropertyType getPathPropertyType() {
        return pathPropertyType;
    }

    public void setPathPropertyType(PathPropertyType pathPropertyType) {
        this.pathPropertyType = pathPropertyType;
    }

    public PathPropertyValue getPathPropertyValue() {
        return pathPropertyValue;
    }

    public void setPathPropertyValue(PathPropertyValue pathPropertyValue) {
        this.pathPropertyValue = pathPropertyValue;
    }

    public Set<PathPropertyValue> getPathPropertyValueList() {
        return pathPropertyValueList;
    }

    public void setPathPropertyValueList(Set<PathPropertyValue> pathPropertyValueList) {
        this.pathPropertyValueList = pathPropertyValueList;
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
        if (!(object instanceof PathProperty)) {
            return false;
        }
        PathProperty other = (PathProperty) object;
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
