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
 * Criteria class for the StockOrderReceive entity. This class is used in StockOrderReceiveResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stock-order-receives?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockOrderReceiveCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter name;

    private IntegerFilter quantity;

    private InstantFilter createdDate;

    private LongFilter creatorId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockOrderReceiveCriteria that = (StockOrderReceiveCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(creatorId, that.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        name,
        quantity,
        createdDate,
        creatorId
        );
    }

    @Override
    public String toString() {
        return "StockOrderReceiveCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
            "}";
    }

}
