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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Purchase entity. This class is used in PurchaseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /purchases?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter totalPayment;

    private InstantFilter createdDate;

    private StringFilter note;

    private LongFilter supplierId;

    private LongFilter creatorId;

    private LongFilter purchaseListId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimalFilter totalPayment) {
        this.totalPayment = totalPayment;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getPurchaseListId() {
        return purchaseListId;
    }

    public void setPurchaseListId(LongFilter purchaseListId) {
        this.purchaseListId = purchaseListId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchaseCriteria that = (PurchaseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(totalPayment, that.totalPayment) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(note, that.note) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(purchaseListId, that.purchaseListId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        totalPayment,
        createdDate,
        note,
        supplierId,
        creatorId,
        purchaseListId
        );
    }

    @Override
    public String toString() {
        return "PurchaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (totalPayment != null ? "totalPayment=" + totalPayment + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (purchaseListId != null ? "purchaseListId=" + purchaseListId + ", " : "") +
            "}";
    }

}
