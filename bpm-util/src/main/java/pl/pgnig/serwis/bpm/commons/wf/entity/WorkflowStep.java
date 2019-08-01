/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.commons.wf.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.Size;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "WORKFLOW_STEP")
@NamedQueries({
    @NamedQuery(name = "WorkflowStep.findAll", query = "SELECT w FROM WorkflowStep w")})
public class WorkflowStep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "STEP_NAME")
    private String stepName;

    @JoinColumn(name = "WORKFLOW_DEFINITION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private WorkflowDefinition workflowDefinition;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nextStep")
    private List<NextWorkflowStep> nextWorkflowStepList;

    public WorkflowStep() {
    }

    public WorkflowStep(Long id) {
        this.id = id;
    }

    public WorkflowStep(Long id, String stepName) {
        this.id = id;
        this.stepName = stepName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public WorkflowDefinition getWorkflowDefinition() {
        return workflowDefinition;
    }

    public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    public List<NextWorkflowStep> getNextWorkflowStepList() {
        return nextWorkflowStepList;
    }

    public void setNextWorkflowStepList(List<NextWorkflowStep> nextWorkflowStepList) {
        this.nextWorkflowStepList = nextWorkflowStepList;
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
        if (!(object instanceof WorkflowStep)) {
            return false;
        }
        WorkflowStep other = (WorkflowStep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.bpm.commons.wf.entity.WorkflowStep[ id=" + id + " ]";
    }
    
}
