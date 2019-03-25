package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A SaleItem.
 */
@Entity
@Table(name = "sale_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "saleitem")
public class SaleItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0)
    @Column(name = "quantity")
    private Integer quantity;

    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private SaleTransactions sale;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("saleItems")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public SaleItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public SaleItem totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public SaleTransactions getSale() {
        return sale;
    }

    public SaleItem sale(SaleTransactions saleTransactions) {
        this.sale = saleTransactions;
        return this;
    }

    public void setSale(SaleTransactions saleTransactions) {
        this.sale = saleTransactions;
    }

    public Product getProduct() {
        return product;
    }

    public SaleItem product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        SaleItem saleItem = (SaleItem) o;
        if (saleItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
