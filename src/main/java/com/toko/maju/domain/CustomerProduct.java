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
 * A CustomerProduct.
 */
@Entity
@Table(name = "customer_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customerproduct")
public class CustomerProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "special_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal specialPrice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("customerProducts")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public CustomerProduct specialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
        return this;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerProduct customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        CustomerProduct customerProduct = (CustomerProduct) o;
        if (customerProduct.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerProduct{" +
            "id=" + getId() +
            ", specialPrice=" + getSpecialPrice() +
            "}";
    }
}
