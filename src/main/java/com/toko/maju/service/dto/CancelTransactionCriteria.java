package com.toko.maju.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the CancelTransaction entity. This class is used in CancelTransactionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /cancel-transactions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CancelTransactionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter noInvoice;

    private InstantFilter cancelDate;

    private StringFilter note;

    private LongFilter saleTransactionsId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNoInvoice() {
        return noInvoice;
    }

    public void setNoInvoice(StringFilter noInvoice) {
        this.noInvoice = noInvoice;
    }

    public InstantFilter getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(InstantFilter cancelDate) {
        this.cancelDate = cancelDate;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getSaleTransactionsId() {
        return saleTransactionsId;
    }

    public void setSaleTransactionsId(LongFilter saleTransactionsId) {
        this.saleTransactionsId = saleTransactionsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CancelTransactionCriteria that = (CancelTransactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(noInvoice, that.noInvoice) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(note, that.note) &&
            Objects.equals(saleTransactionsId, that.saleTransactionsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        noInvoice,
        cancelDate,
        note,
        saleTransactionsId
        );
    }

    @Override
    public String toString() {
        return "CancelTransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (noInvoice != null ? "noInvoice=" + noInvoice + ", " : "") +
                (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (saleTransactionsId != null ? "saleTransactionsId=" + saleTransactionsId + ", " : "") +
            "}";
    }

}
