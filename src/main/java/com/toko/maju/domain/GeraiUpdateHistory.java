package com.toko.maju.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A GeraiUpdateHistory.
 */
@Entity
@Table(name = "gerai_update_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "geraiupdatehistory")
public class GeraiUpdateHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "last_sale_id", nullable = false)
    private Long lastSaleId;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Column(name = "sale_date", nullable = false)
    private Instant saleDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastSaleId() {
        return lastSaleId;
    }

    public GeraiUpdateHistory lastSaleId(Long lastSaleId) {
        this.lastSaleId = lastSaleId;
        return this;
    }

    public void setLastSaleId(Long lastSaleId) {
        this.lastSaleId = lastSaleId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public GeraiUpdateHistory createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public GeraiUpdateHistory saleDate(Instant saleDate) {
        this.saleDate = saleDate;
        return this;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
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
        GeraiUpdateHistory geraiUpdateHistory = (GeraiUpdateHistory) o;
        if (geraiUpdateHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geraiUpdateHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeraiUpdateHistory{" +
            "id=" + getId() +
            ", lastSaleId=" + getLastSaleId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", saleDate='" + getSaleDate() + "'" +
            "}";
    }
}
