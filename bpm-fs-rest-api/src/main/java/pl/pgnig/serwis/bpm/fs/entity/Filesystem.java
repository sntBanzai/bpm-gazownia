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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "filesystem")
@NamedEntityGraph(name = "Filesystem.wholeGraph", attributeNodes = {
    @NamedAttributeNode(value = "filesystemActorList", subgraph = "filesystemActor-subgraph"),
    @NamedAttributeNode(value = "pathList", subgraph = "path-subgraph")}, subgraphs = {
    @NamedSubgraph(name = "path-subgraph", attributeNodes = {
        @NamedAttributeNode("value"),
        @NamedAttributeNode(value = "parentPath", subgraph = "parentPath-subgraph"),
        @NamedAttributeNode(value = "pathContent", subgraph = "pathContent-subgraph")}),
    @NamedSubgraph(name = "filesystemActor-subgraph", attributeNodes = {
        @NamedAttributeNode("name"),
        @NamedAttributeNode("idGroup")}),
    @NamedSubgraph(name = "pathContent-subgraph", attributeNodes = {
        @NamedAttributeNode("content")}),
    @NamedSubgraph(name = "parentPath-subgraph", attributeNodes = {
        @NamedAttributeNode("value"),
        @NamedAttributeNode("directory")})})
public class Filesystem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filesystem")
    private Set<Path> pathList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filesystem")
    private Set<FilesystemActor> filesystemActorList;

    public Filesystem() {
    }

    public Filesystem(Long id) {
        this.id = id;
    }

    public Filesystem(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Path> getPathList() {
        return pathList;
    }

    public void setPathList(Set<Path> pathList) {
        this.pathList = pathList;
    }

    public Set<FilesystemActor> getFilesystemActorList() {
        return filesystemActorList;
    }

    public void setFilesystemActorList(Set<FilesystemActor> filesystemActorList) {
        this.filesystemActorList = filesystemActorList;
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
        if (!(object instanceof Filesystem)) {
            return false;
        }
        Filesystem other = (Filesystem) object;
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
