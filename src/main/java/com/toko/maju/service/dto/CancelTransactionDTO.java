package com.toko.maju.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CancelTransaction entity.
 */
public class CancelTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private String noInvoice;

    @NotNull
    private Instant cancelDate;

    @NotNull
    private String note;


    private Long saleTransactionsId;

    private String saleTransactionsNoInvoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoInvoice() {
        return noInvoice;
    }

    public void setNoInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
    }

    public Instant getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Instant cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getSaleTransactionsId() {
        return saleTransactionsId;
    }

    public void setSaleTransactionsId(Long saleTransactionsId) {
        this.saleTransactionsId = saleTransactionsId;
    }

    public String getSaleTransactionsNoInvoice() {
        return saleTransactionsNoInvoice;
    }

    public void setSaleTransactionsNoInvoice(String saleTransactionsNoInvoice) {
        this.saleTransactionsNoInvoice = saleTransactionsNoInvoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CancelTransactionDTO cancelTransactionDTO = (CancelTransactionDTO) o;
        if (cancelTransactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cancelTransactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CancelTransactionDTO{" +
            "id=" + getId() +
            ", noInvoice='" + getNoInvoice() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", note='" + getNote() + "'" +
            ", saleTransactions=" + getSaleTransactionsId() +
            ", saleTransactions='" + getSaleTransactionsNoInvoice() + "'" +
            "}";
    }
}
