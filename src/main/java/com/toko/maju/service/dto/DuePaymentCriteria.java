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
 * Criteria class for the DuePayment entity. This class is used in DuePaymentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /due-payments?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DuePaymentCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter remainingPayment;

    private InstantFilter createdDate;

    private BooleanFilter settled;

    private BigDecimalFilter paid;

    private LongFilter creatorId;

    private LongFilter saleId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(BigDecimalFilter remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public BooleanFilter getSettled() {
        return settled;
    }

    public void setSettled(BooleanFilter settled) {
        this.settled = settled;
    }

    public BigDecimalFilter getPaid() {
        return paid;
    }

    public void setPaid(BigDecimalFilter paid) {
        this.paid = paid;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getSaleId() {
        return saleId;
    }

    public void setSaleId(LongFilter saleId) {
        this.saleId = saleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DuePaymentCriteria that = (DuePaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(remainingPayment, that.remainingPayment) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(settled, that.settled) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(saleId, that.saleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        remainingPayment,
        createdDate,
        settled,
        paid,
        creatorId,
        saleId
        );
    }

    @Override
    public String toString() {
        return "DuePaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (remainingPayment != null ? "remainingPayment=" + remainingPayment + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (settled != null ? "settled=" + settled + ", " : "") +
                (paid != null ? "paid=" + paid + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (saleId != null ? "saleId=" + saleId + ", " : "") +
            "}";
    }

}
