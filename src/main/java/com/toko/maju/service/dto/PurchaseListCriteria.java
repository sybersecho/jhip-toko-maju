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

/**
 * Criteria class for the PurchaseList entity. This class is used in PurchaseListResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /purchase-lists?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseListCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter name;

    private StringFilter unit;

    private BigDecimalFilter unitPrice;

    private IntegerFilter quantity;

    private BigDecimalFilter total;

    private LongFilter purchaseId;

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

    public StringFilter getUnit() {
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
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

    public BigDecimalFilter getTotal() {
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public LongFilter getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(LongFilter purchaseId) {
        this.purchaseId = purchaseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchaseListCriteria that = (PurchaseListCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(total, that.total) &&
            Objects.equals(purchaseId, that.purchaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        name,
        unit,
        unitPrice,
        quantity,
        total,
        purchaseId
        );
    }

    @Override
    public String toString() {
        return "PurchaseListCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (unit != null ? "unit=" + unit + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (purchaseId != null ? "purchaseId=" + purchaseId + ", " : "") +
            "}";
    }

}
