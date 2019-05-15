package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.toko.maju.domain.enumeration.TransactionType;

/**
 * A ReturnTransaction.
 */
@Entity
@Table(name = "return_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "returntransaction")
public class ReturnTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private Instant created_date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @NotNull
    @Column(name = "total_price_return", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPriceReturn;

    @ManyToOne
    @JsonIgnoreProperties("returnTransactions")
    private User creator;

    @ManyToOne
    @JsonIgnoreProperties("returnTransactions")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("returnTransactions")
    private Supplier supplier;

    @OneToMany(mappedBy = "returnTransaction", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReturnItem> returnItems = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated_date() {
        return created_date;
    }

    public ReturnTransaction created_date(Instant created_date) {
        this.created_date = created_date;
        return this;
    }

    public void setCreated_date(Instant created_date) {
        this.created_date = created_date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public ReturnTransaction transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTotalPriceReturn() {
        return totalPriceReturn;
    }

    public ReturnTransaction totalPriceReturn(BigDecimal totalPriceReturn) {
        this.totalPriceReturn = totalPriceReturn;
        return this;
    }

    public void setTotalPriceReturn(BigDecimal totalPriceReturn) {
        this.totalPriceReturn = totalPriceReturn;
    }

    public User getCreator() {
        return creator;
    }

    public ReturnTransaction creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ReturnTransaction customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public ReturnTransaction supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Set<ReturnItem> getReturnItems() {
        return returnItems;
    }

    public ReturnTransaction returnItems(Set<ReturnItem> returnItems) {
        this.returnItems = returnItems;
        return this;
    }

    public ReturnTransaction addReturnItem(ReturnItem returnItem) {
        this.returnItems.add(returnItem);
        returnItem.setReturnTransaction(this);
        return this;
    }

    public ReturnTransaction removeReturnItem(ReturnItem returnItem) {
        this.returnItems.remove(returnItem);
        returnItem.setReturnTransaction(null);
        return this;
    }

    public void setReturnItems(Set<ReturnItem> returnItems) {
        this.returnItems = returnItems;
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
        ReturnTransaction returnTransaction = (ReturnTransaction) o;
        if (returnTransaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), returnTransaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReturnTransaction{" +
            "id=" + getId() +
            ", created_date='" + getCreated_date() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", totalPriceReturn=" + getTotalPriceReturn() +
            "}";
    }
}
