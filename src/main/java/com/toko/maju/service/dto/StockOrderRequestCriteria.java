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
 * Criteria class for the StockOrderRequest entity. This class is used in StockOrderRequestResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stock-order-requests?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockOrderRequestCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter name;

    private StringFilter unitName;

    private BigDecimalFilter unitPrice;

    private IntegerFilter quantity;

    private BigDecimalFilter totalPrice;

    private InstantFilter createdDate;

    private LongFilter stockOrderId;

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

    public StringFilter getUnitName() {
        return unitName;
    }

    public void setUnitName(StringFilter unitName) {
        this.unitName = unitName;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getStockOrderId() {
        return stockOrderId;
    }

    public void setStockOrderId(LongFilter stockOrderId) {
        this.stockOrderId = stockOrderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockOrderRequestCriteria that = (StockOrderRequestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(unitName, that.unitName) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(stockOrderId, that.stockOrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        name,
        unitName,
        unitPrice,
        quantity,
        totalPrice,
        createdDate,
        stockOrderId
        );
    }

    @Override
    public String toString() {
        return "StockOrderRequestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (unitName != null ? "unitName=" + unitName + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (stockOrderId != null ? "stockOrderId=" + stockOrderId + ", " : "") +
            "}";
    }

}
