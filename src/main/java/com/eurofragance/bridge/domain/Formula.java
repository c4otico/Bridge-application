package com.eurofragance.bridge.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Formula.
 */
@Entity
@Table(name = "formula")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formula")
public class Formula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "formula_name")
    private String formulaName;

    @ManyToOne
    @JsonIgnoreProperties("formulas")
    private Applications concreteApplication;

    @ManyToOne
    @JsonIgnoreProperties("formulas")
    private FormulaStatus formulaStatus;

    @ManyToOne
    @JsonIgnoreProperties("formulas")
    private Developer owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public Formula formulaName(String formulaName) {
        this.formulaName = formulaName;
        return this;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public Applications getConcreteApplication() {
        return concreteApplication;
    }

    public Formula concreteApplication(Applications applications) {
        this.concreteApplication = applications;
        return this;
    }

    public void setConcreteApplication(Applications applications) {
        this.concreteApplication = applications;
    }

    public FormulaStatus getFormulaStatus() {
        return formulaStatus;
    }

    public Formula formulaStatus(FormulaStatus formulaStatus) {
        this.formulaStatus = formulaStatus;
        return this;
    }

    public void setFormulaStatus(FormulaStatus formulaStatus) {
        this.formulaStatus = formulaStatus;
    }

    public Developer getOwner() {
        return owner;
    }

    public Formula owner(Developer developer) {
        this.owner = developer;
        return this;
    }

    public void setOwner(Developer developer) {
        this.owner = developer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formula)) {
            return false;
        }
        return id != null && id.equals(((Formula) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Formula{" +
            "id=" + getId() +
            ", formulaName='" + getFormulaName() + "'" +
            "}";
    }
}
