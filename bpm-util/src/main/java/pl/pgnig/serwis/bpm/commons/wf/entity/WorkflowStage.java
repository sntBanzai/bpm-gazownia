/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.commons.wf.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "WORKFLOW_STAGE")
@NamedQueries({
    @NamedQuery(name = "WorkflowStage.findAll", query = "SELECT w FROM WorkflowStage w")})
public class WorkflowStage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "WORKFLOW_INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private WorkflowInstance workflowInstance;
    
    @JoinColumn(name = "PREVIOUS_STAGE_ID", referencedColumnName = "ID")
    @ManyToOne
    private WorkflowStage previousStage;
    @JoinColumn(name = "WORKFLOW_STEP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private WorkflowStep workflowStep;

    public WorkflowStage() {
    }

    public WorkflowStage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowInstance getWorkflowInstance() {
        return workflowInstance;
    }

    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }

    public WorkflowStage getPreviousStage() {
        return previousStage;
    }

    public void setPreviousStage(WorkflowStage previousStage) {
        this.previousStage = previousStage;
    }

    public WorkflowStep getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(WorkflowStep workflowStep) {
        this.workflowStep = workflowStep;
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
        if (!(object instanceof WorkflowStage)) {
            return false;
        }
        WorkflowStage other = (WorkflowStage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.bpm.commons.wf.entity.WorkflowStage[ id=" + id + " ]";
    }
    
}
