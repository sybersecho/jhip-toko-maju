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
 * Criteria class for the StockOrderProcess entity. This class is used in StockOrderProcessResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stock-order-processes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockOrderProcessCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter name;

    private IntegerFilter quantityRequest;

    private IntegerFilter stockInHand;

    private IntegerFilter quantityApprove;

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

    public IntegerFilter getQuantityRequest() {
        return quantityRequest;
    }

    public void setQuantityRequest(IntegerFilter quantityRequest) {
        this.quantityRequest = quantityRequest;
    }

    public IntegerFilter getStockInHand() {
        return stockInHand;
    }

    public void setStockInHand(IntegerFilter stockInHand) {
        this.stockInHand = stockInHand;
    }

    public IntegerFilter getQuantityApprove() {
        return quantityApprove;
    }

    public void setQuantityApprove(IntegerFilter quantityApprove) {
        this.quantityApprove = quantityApprove;
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
        final StockOrderProcessCriteria that = (StockOrderProcessCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(quantityRequest, that.quantityRequest) &&
            Objects.equals(stockInHand, that.stockInHand) &&
            Objects.equals(quantityApprove, that.quantityApprove) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(creatorId, that.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        name,
        quantityRequest,
        stockInHand,
        quantityApprove,
        createdDate,
        creatorId
        );
    }

    @Override
    public String toString() {
        return "StockOrderProcessCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (quantityRequest != null ? "quantityRequest=" + quantityRequest + ", " : "") +
                (stockInHand != null ? "stockInHand=" + stockInHand + ", " : "") +
                (quantityApprove != null ? "quantityApprove=" + quantityApprove + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
            "}";
    }

}
