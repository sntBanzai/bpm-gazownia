/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.commons.wf.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jerzy.malyszko
 */
@Entity
@Table(name = "WORKFLOW_DEFINITION")
@NamedQueries({
    @NamedQuery(name = "WorkflowDefinition.findAll", query = "SELECT w FROM WorkflowDefinition w")})
public class WorkflowDefinition implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDER_NUMBER")
    private short orderNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "WORKFLOW_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Workflow workflow;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workflowDefinition")
    private List<WorkflowStep> workflowStepList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workflowDefinition")
    private List<WorkflowInstance> workflowInstanceList;

    public WorkflowDefinition() {
    }

    public WorkflowDefinition(Long id) {
        this.id = id;
    }

    public WorkflowDefinition(Long id, short orderNumber, Date creationDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(short orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<WorkflowStep> getWorkflowStepList() {
        return workflowStepList;
    }

    public void setWorkflowStepList(List<WorkflowStep> workflowStepList) {
        this.workflowStepList = workflowStepList;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public List<WorkflowInstance> getWorkflowInstanceList() {
        return workflowInstanceList;
    }

    public void setWorkflowInstanceList(List<WorkflowInstance> workflowInstanceList) {
        this.workflowInstanceList = workflowInstanceList;
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
        if (!(object instanceof WorkflowDefinition)) {
            return false;
        }
        WorkflowDefinition other = (WorkflowDefinition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.bpm.commons.wf.entity.WorkflowDefinition[ id=" + id + " ]";
    }

}
