package com.toko.maju.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.toko.maju.domain.enumeration.ProductStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the ReturnItem entity. This class is used in ReturnItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /return-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReturnItemCriteria implements Serializable {
    /**
     * Class for filtering ProductStatus
     */
    public static class ProductStatusFilter extends Filter<ProductStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter productName;

    private IntegerFilter quantity;

    private BigDecimalFilter unitPrice;

    private ProductStatusFilter productStatus;

    private StringFilter unit;

    private BigDecimalFilter totalItemPrice;

    private LongFilter productId;

    private LongFilter returnTransactionId;

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

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ProductStatusFilter getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatusFilter productStatus) {
        this.productStatus = productStatus;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public BigDecimalFilter getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(BigDecimalFilter totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getReturnTransactionId() {
        return returnTransactionId;
    }

    public void setReturnTransactionId(LongFilter returnTransactionId) {
        this.returnTransactionId = returnTransactionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReturnItemCriteria that = (ReturnItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(productStatus, that.productStatus) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(totalItemPrice, that.totalItemPrice) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(returnTransactionId, that.returnTransactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        productName,
        quantity,
        unitPrice,
        productStatus,
        unit,
        totalItemPrice,
        productId,
        returnTransactionId
        );
    }

    @Override
    public String toString() {
        return "ReturnItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (productName != null ? "productName=" + productName + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (productStatus != null ? "productStatus=" + productStatus + ", " : "") +
                (unit != null ? "unit=" + unit + ", " : "") +
                (totalItemPrice != null ? "totalItemPrice=" + totalItemPrice + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
                (returnTransactionId != null ? "returnTransactionId=" + returnTransactionId + ", " : "") +
            "}";
    }

}
