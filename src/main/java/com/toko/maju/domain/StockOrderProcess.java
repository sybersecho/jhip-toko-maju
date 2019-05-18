package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A StockOrderProcess.
 */
@Entity
@Table(name = "stock_order_process")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stockorderprocess")
public class StockOrderProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity_request")
    private Integer quantityRequest;

    @Column(name = "stock_in_hand")
    private Integer stockInHand;

    @Column(name = "quantity_approve")
    private Integer quantityApprove;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToOne
    @JsonIgnoreProperties("stockOrderProcesses")
    private User creator;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public StockOrderProcess barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public StockOrderProcess name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityRequest() {
        return quantityRequest;
    }

    public StockOrderProcess quantityRequest(Integer quantityRequest) {
        this.quantityRequest = quantityRequest;
        return this;
    }

    public void setQuantityRequest(Integer quantityRequest) {
        this.quantityRequest = quantityRequest;
    }

    public Integer getStockInHand() {
        return stockInHand;
    }

    public StockOrderProcess stockInHand(Integer stockInHand) {
        this.stockInHand = stockInHand;
        return this;
    }

    public void setStockInHand(Integer stockInHand) {
        this.stockInHand = stockInHand;
    }

    public Integer getQuantityApprove() {
        return quantityApprove;
    }

    public StockOrderProcess quantityApprove(Integer quantityApprove) {
        this.quantityApprove = quantityApprove;
        return this;
    }

    public void setQuantityApprove(Integer quantityApprove) {
        this.quantityApprove = quantityApprove;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public StockOrderProcess createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreator() {
        return creator;
    }

    public StockOrderProcess creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
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
        StockOrderProcess stockOrderProcess = (StockOrderProcess) o;
        if (stockOrderProcess.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrderProcess.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrderProcess{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", name='" + getName() + "'" +
            ", quantityRequest=" + getQuantityRequest() +
            ", stockInHand=" + getStockInHand() +
            ", quantityApprove=" + getQuantityApprove() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
