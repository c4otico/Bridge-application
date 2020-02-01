package com.eurofragance.bridge.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A FormulaStatus.
 */
@Entity
@Table(name = "formula_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formulastatus")
public class FormulaStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name_status")
    private String nameStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public FormulaStatus nameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
        return this;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormulaStatus)) {
            return false;
        }
        return id != null && id.equals(((FormulaStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FormulaStatus{" +
            "id=" + getId() +
            ", nameStatus='" + getNameStatus() + "'" +
            "}";
    }
}
