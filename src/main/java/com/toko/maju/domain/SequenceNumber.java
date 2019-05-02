package com.toko.maju.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SequenceNumber.
 */
@Entity
@Table(name = "sequence_number")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sequencenumber")
public class SequenceNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "seq_type", nullable = false, unique = true)
    private String type;

    @Column(name = "next_value")
    private Integer nextValue;

    @Column(name = "increment_value")
    private Integer incrementValue;

    @NotNull
    @Column(name = "display", nullable = false)
    private String display;

    @NotNull
    @Column(name = "code_type", nullable = false)
    private String codeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public SequenceNumber type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNextValue() {
        return nextValue;
    }

    public SequenceNumber nextValue(Integer nextValue) {
        this.nextValue = nextValue;
        return this;
    }

    public void setNextValue(Integer nextValue) {
        this.nextValue = nextValue;
    }

    public Integer getIncrementValue() {
        return incrementValue;
    }

    public SequenceNumber incrementValue(Integer incrementValue) {
        this.incrementValue = incrementValue;
        return this;
    }

    public void setIncrementValue(Integer incrementValue) {
        this.incrementValue = incrementValue;
    }

    public String getDisplay() {
        return display;
    }

    public SequenceNumber display(String display) {
        this.display = display;
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getCodeType() {
        return codeType;
    }

    public SequenceNumber codeType(String codeType) {
        this.codeType = codeType;
        return this;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SequenceNumber sequenceNumber = (SequenceNumber) o;
        if (sequenceNumber.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sequenceNumber.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SequenceNumber{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", nextValue=" + getNextValue() +
            ", incrementValue=" + getIncrementValue() +
            ", display='" + getDisplay() + "'" +
            ", codeType='" + getCodeType() + "'" +
            "}";
    }
}
