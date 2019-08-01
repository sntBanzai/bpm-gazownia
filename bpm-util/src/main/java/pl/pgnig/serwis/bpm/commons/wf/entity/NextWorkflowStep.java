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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "NEXT_WORKFLOW_STEP")
@NamedQueries({
    @NamedQuery(name = "NextWorkflowStep.findAll", query = "SELECT n FROM NextWorkflowStep n")})
public class NextWorkflowStep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "NEXT_STEP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private WorkflowStep nextStep;
    @JoinColumn(name = "PREVIOUS_STEP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private WorkflowStep previousStep;

    public NextWorkflowStep() {
    }

    public NextWorkflowStep(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowStep getNextStep() {
        return nextStep;
    }

    public void setNextStep(WorkflowStep nextStep) {
        this.nextStep = nextStep;
    }

    public WorkflowStep getPreviousStep() {
        return previousStep;
    }

    public void setPreviousStep(WorkflowStep previousStep) {
        this.previousStep = previousStep;
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
        if (!(object instanceof NextWorkflowStep)) {
            return false;
        }
        NextWorkflowStep other = (NextWorkflowStep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.bpm.commons.wf.entity.NextWorkflowStep[ id=" + id + " ]";
    }
    
}
