/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.commons.wf.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "WORKFLOW_INSTANCE")
@NamedQueries({
    @NamedQuery(name = "WorkflowInstance.findAll", query = "SELECT w FROM WorkflowInstance w")})
public class WorkflowInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "CURRENT_WORKFLOW_STAGE_ID", referencedColumnName = "ID")
    @OneToOne
    private WorkflowStage currentWorkflowStage;
    @JoinColumn(name = "WORKFLOW_DEFINITION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private WorkflowDefinition workflowDefinition;

    public WorkflowInstance() {
    }

    public WorkflowInstance(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowStage getCurrentWorkflowStage() {
        return currentWorkflowStage;
    }

    public void setCurrentWorkflowStage(WorkflowStage currentWorkflowStage) {
        this.currentWorkflowStage = currentWorkflowStage;
    }

    public WorkflowDefinition getWorkflowDefinition() {
        return workflowDefinition;
    }

    public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
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
        if (!(object instanceof WorkflowInstance)) {
            return false;
        }
        WorkflowInstance other = (WorkflowInstance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.bpm.commons.wf.entity.WorkflowInstance[ id=" + id + " ]";
    }
    
}
