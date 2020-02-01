package com.eurofragance.bridge.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A SendToUser.
 */
@Entity
@Table(name = "send_to_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sendtouser")
public class SendToUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    @JsonIgnoreProperties("sendToUsers")
    private Formula formula;

    @ManyToOne
    @JsonIgnoreProperties("sendToUsers")
    private Developer developer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public SendToUser creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Formula getFormula() {
        return formula;
    }

    public SendToUser formula(Formula formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public SendToUser developer(Developer developer) {
        this.developer = developer;
        return this;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendToUser)) {
            return false;
        }
        return id != null && id.equals(((SendToUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SendToUser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
