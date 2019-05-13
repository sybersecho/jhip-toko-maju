package com.toko.maju.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.toko.maju.domain.enumeration.TransactionType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the ReturnTransaction entity. This class is used in ReturnTransactionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /return-transactions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReturnTransactionCriteria implements Serializable {
    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter created_date;

    private TransactionTypeFilter transactionType;

    private LongFilter creatorId;

    private LongFilter customerId;

    private LongFilter supplierId;

    private LongFilter returnItemId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreated_date() {
        return created_date;
    }

    public void setCreated_date(InstantFilter created_date) {
        this.created_date = created_date;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public LongFilter getReturnItemId() {
        return returnItemId;
    }

    public void setReturnItemId(LongFilter returnItemId) {
        this.returnItemId = returnItemId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReturnTransactionCriteria that = (ReturnTransactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(created_date, that.created_date) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(returnItemId, that.returnItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        created_date,
        transactionType,
        creatorId,
        customerId,
        supplierId,
        returnItemId
        );
    }

    @Override
    public String toString() {
        return "ReturnTransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (created_date != null ? "created_date=" + created_date + ", " : "") +
                (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
                (returnItemId != null ? "returnItemId=" + returnItemId + ", " : "") +
            "}";
    }

}
