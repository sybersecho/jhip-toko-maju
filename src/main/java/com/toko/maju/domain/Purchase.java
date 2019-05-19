package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
 * A Purchase.
 */
@Entity
@Table(name = "purchase")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchase")
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_payment", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPayment;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "note")
    private String note;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("purchases")
    private Supplier supplier;

    @ManyToOne
    @JsonIgnoreProperties("purchases")
    private User creator;

    @OneToMany(mappedBy = "purchase", cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonManagedReference
    private Set<PurchaseList> purchaseLists = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public Purchase totalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
        return this;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Purchase createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getNote() {
        return note;
    }

    public Purchase note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Purchase supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public User getCreator() {
        return creator;
    }

    public Purchase creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Set<PurchaseList> getPurchaseLists() {
        return purchaseLists;
    }

    public Purchase purchaseLists(Set<PurchaseList> purchaseLists) {
        this.purchaseLists = purchaseLists;
        return this;
    }

    public Purchase addPurchaseList(PurchaseList purchaseList) {
        this.purchaseLists.add(purchaseList);
        purchaseList.setPurchase(this);
        return this;
    }

    public Purchase removePurchaseList(PurchaseList purchaseList) {
        this.purchaseLists.remove(purchaseList);
        purchaseList.setPurchase(null);
        return this;
    }

    public void setPurchaseLists(Set<PurchaseList> purchaseLists) {
        this.purchaseLists = purchaseLists;
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
        Purchase purchase = (Purchase) o;
        if (purchase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Purchase{" +
            "id=" + getId() +
            ", totalPayment=" + getTotalPayment() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
