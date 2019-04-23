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

/**
 * A SaleTransactions.
 */
@Entity
@Table(name = "sale_transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "saletransactions")
public class SaleTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no_invoice")
    private String noInvoice;

    @DecimalMin(value = "0")
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_payment", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPayment;

    @DecimalMin(value = "0")
    @Column(name = "remaining_payment", precision = 10, scale = 2)
    private BigDecimal remainingPayment;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "paid", precision = 10, scale = 2, nullable = false)
    private BigDecimal paid;

    @Column(name = "sale_date")
    private Instant saleDate;

    @NotNull
    @Column(name = "settled", nullable = false)
    private Boolean settled;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SaleItem> items = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("saleTransactions")
    private Customer customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("saleTransactions")
    private User creator;

    @OneToMany(mappedBy = "sales")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DuePayment> duePayments = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoInvoice() {
        return noInvoice;
    }

    public SaleTransactions noInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
        return this;
    }

    public void setNoInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public SaleTransactions discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public SaleTransactions totalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
        return this;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getRemainingPayment() {
        return remainingPayment;
    }

    public SaleTransactions remainingPayment(BigDecimal remainingPayment) {
        this.remainingPayment = remainingPayment;
        return this;
    }

    public void setRemainingPayment(BigDecimal remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public SaleTransactions paid(BigDecimal paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public SaleTransactions saleDate(Instant saleDate) {
        this.saleDate = saleDate;
        return this;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public Boolean isSettled() {
        return settled;
    }

    public SaleTransactions settled(Boolean settled) {
        this.settled = settled;
        return this;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public Set<SaleItem> getItems() {
        return items;
    }

    public SaleTransactions items(Set<SaleItem> saleItems) {
        this.items = saleItems;
        return this;
    }

    public SaleTransactions addItems(SaleItem saleItem) {
        this.items.add(saleItem);
        saleItem.setSale(this);
        return this;
    }

    public SaleTransactions removeItems(SaleItem saleItem) {
        this.items.remove(saleItem);
        saleItem.setSale(null);
        return this;
    }

    public void setItems(Set<SaleItem> saleItems) {
        this.items = saleItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public SaleTransactions customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getCreator() {
        return creator;
    }

    public SaleTransactions creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Set<DuePayment> getDuePayments() {
        return duePayments;
    }

    public SaleTransactions duePayments(Set<DuePayment> duePayments) {
        this.duePayments = duePayments;
        return this;
    }

    public SaleTransactions addDuePayment(DuePayment duePayment) {
        this.duePayments.add(duePayment);
        duePayment.setSales(this);
        return this;
    }

    public SaleTransactions removeDuePayment(DuePayment duePayment) {
        this.duePayments.remove(duePayment);
        duePayment.setSales(null);
        return this;
    }

    public void setDuePayments(Set<DuePayment> duePayments) {
        this.duePayments = duePayments;
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
        SaleTransactions saleTransactions = (SaleTransactions) o;
        if (saleTransactions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleTransactions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleTransactions{" +
            "id=" + getId() +
            ", noInvoice='" + getNoInvoice() + "'" +
            ", discount=" + getDiscount() +
            ", totalPayment=" + getTotalPayment() +
            ", remainingPayment=" + getRemainingPayment() +
            ", paid=" + getPaid() +
            ", saleDate='" + getSaleDate() + "'" +
            ", settled='" + isSettled() + "'" +
            "}";
    }
}
