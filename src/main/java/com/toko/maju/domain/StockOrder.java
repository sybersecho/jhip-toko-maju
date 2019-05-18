package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A StockOrder.
 */
@Entity
@Table(name = "stock_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stockorder")
public class StockOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "site_location", nullable = false)
    private String siteLocation;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Column(name = "processed", nullable = false)
    private Boolean processed;

    @Column(name = "processed_date")
    private Instant processedDate;

    @ManyToOne
    @JsonIgnoreProperties("stockOrders")
    private User creator;

    @ManyToOne
    @JsonIgnoreProperties("stockOrders")
    private User approval;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public StockOrder siteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
        return this;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public StockOrder createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isProcessed() {
        return processed;
    }

    public StockOrder processed(Boolean processed) {
        this.processed = processed;
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Instant getProcessedDate() {
        return processedDate;
    }

    public StockOrder processedDate(Instant processedDate) {
        this.processedDate = processedDate;
        return this;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    public User getCreator() {
        return creator;
    }

    public StockOrder creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public User getApproval() {
        return approval;
    }

    public StockOrder approval(User user) {
        this.approval = user;
        return this;
    }

    public void setApproval(User user) {
        this.approval = user;
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
        StockOrder stockOrder = (StockOrder) o;
        if (stockOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrder{" +
            "id=" + getId() +
            ", siteLocation='" + getSiteLocation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", processed='" + isProcessed() + "'" +
            ", processedDate='" + getProcessedDate() + "'" +
            "}";
    }
}
