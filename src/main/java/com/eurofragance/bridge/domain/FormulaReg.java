package com.eurofragance.bridge.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "formula_reg")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formulareg")
public class FormulaReg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "more_details")
    private String moreDetails;

    @Column(name = "even_more_details")
    private String evenMoreDetails;

    @ManyToOne
    @JsonIgnoreProperties("formulaRegs")
    private Formula formula;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public FormulaReg moreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
        return this;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getEvenMoreDetails() {
        return evenMoreDetails;
    }

    public FormulaReg evenMoreDetails(String evenMoreDetails) {
        this.evenMoreDetails = evenMoreDetails;
        return this;
    }

    public void setEvenMoreDetails(String evenMoreDetails) {
        this.evenMoreDetails = evenMoreDetails;
    }

    public Formula getFormula() {
        return formula;
    }

    public FormulaReg formula(Formula formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormulaReg)) {
            return false;
        }
        return id != null && id.equals(((FormulaReg) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FormulaReg{" +
            "id=" + getId() +
            ", moreDetails='" + getMoreDetails() + "'" +
            ", evenMoreDetails='" + getEvenMoreDetails() + "'" +
            "}";
    }
}
