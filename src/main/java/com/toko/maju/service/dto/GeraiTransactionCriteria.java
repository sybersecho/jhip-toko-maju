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
 * Criteria class for the GeraiTransaction entity. This class is used in GeraiTransactionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /gerai-transactions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeraiTransactionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter name;

    private IntegerFilter quantity;

    private IntegerFilter currentStock;

    private InstantFilter receivedDate;

    private LongFilter geraiId;

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

    public IntegerFilter getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(IntegerFilter currentStock) {
        this.currentStock = currentStock;
    }

    public InstantFilter getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(InstantFilter receivedDate) {
        this.receivedDate = receivedDate;
    }

    public LongFilter getGeraiId() {
        return geraiId;
    }

    public void setGeraiId(LongFilter geraiId) {
        this.geraiId = geraiId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeraiTransactionCriteria that = (GeraiTransactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(currentStock, that.currentStock) &&
            Objects.equals(receivedDate, that.receivedDate) &&
            Objects.equals(geraiId, that.geraiId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        name,
        quantity,
        currentStock,
        receivedDate,
        geraiId
        );
    }

    @Override
    public String toString() {
        return "GeraiTransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (currentStock != null ? "currentStock=" + currentStock + ", " : "") +
                (receivedDate != null ? "receivedDate=" + receivedDate + ", " : "") +
                (geraiId != null ? "geraiId=" + geraiId + ", " : "") +
            "}";
    }

}
