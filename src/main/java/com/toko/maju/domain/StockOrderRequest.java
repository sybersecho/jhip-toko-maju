package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A StockOrderRequest.
 */
@Entity
@Table(name = "stock_order_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stockorderrequest")
public class StockOrderRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToOne
    @JsonIgnoreProperties("stockOrderRequests")
    private StockOrder stockOrder;

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

    public StockOrderRequest barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public StockOrderRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public StockOrderRequest unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public StockOrderRequest unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public StockOrderRequest quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public StockOrderRequest totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public StockOrderRequest createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public StockOrder getStockOrder() {
        return stockOrder;
    }

    public StockOrderRequest stockOrder(StockOrder stockOrder) {
        this.stockOrder = stockOrder;
        return this;
    }

    public void setStockOrder(StockOrder stockOrder) {
        this.stockOrder = stockOrder;
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
        StockOrderRequest stockOrderRequest = (StockOrderRequest) o;
        if (stockOrderRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrderRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrderRequest{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", name='" + getName() + "'" +
            ", unitName='" + getUnitName() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
