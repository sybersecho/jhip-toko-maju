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
 * A CancelTransaction.
 */
@Entity
@Table(name = "cancel_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "canceltransaction")
public class CancelTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "no_invoice", nullable = false)
    private String noInvoice;

    @NotNull
    @Column(name = "cancel_date", nullable = false)
    private Instant cancelDate;

    @NotNull
    @Column(name = "note", nullable = false)
    private String note;

    @NotNull
    @Column(name = "no_cancel_invoice", nullable = false)
    private String noCancelInvoice;

    @OneToOne
    @JoinColumn(unique = true)
    private SaleTransactions saleTransactions;

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

    public CancelTransaction noInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
        return this;
    }

    public void setNoInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
    }

    public Instant getCancelDate() {
        return cancelDate;
    }

    public CancelTransaction cancelDate(Instant cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(Instant cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getNote() {
        return note;
    }

    public CancelTransaction note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoCancelInvoice() {
        return noCancelInvoice;
    }

    public CancelTransaction noCancelInvoice(String noCancelInvoice) {
        this.noCancelInvoice = noCancelInvoice;
        return this;
    }

    public void setNoCancelInvoice(String noCancelInvoice) {
        this.noCancelInvoice = noCancelInvoice;
    }

    public SaleTransactions getSaleTransactions() {
        return saleTransactions;
    }

    public CancelTransaction saleTransactions(SaleTransactions saleTransactions) {
        this.saleTransactions = saleTransactions;
        return this;
    }

    public void setSaleTransactions(SaleTransactions saleTransactions) {
        this.saleTransactions = saleTransactions;
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
        CancelTransaction cancelTransaction = (CancelTransaction) o;
        if (cancelTransaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cancelTransaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CancelTransaction{" +
            "id=" + getId() +
            ", noInvoice='" + getNoInvoice() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", note='" + getNote() + "'" +
            ", noCancelInvoice='" + getNoCancelInvoice() + "'" +
            "}";
    }
}
