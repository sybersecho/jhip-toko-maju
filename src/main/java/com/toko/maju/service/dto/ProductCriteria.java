package com.toko.maju.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.toko.maju.domain.enumeration.UnitMeasure;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the Product entity. This class is used in ProductResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /products?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable {
    /**
     * Class for filtering UnitMeasure
     */
    public static class UnitMeasureFilter extends Filter<UnitMeasure> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter name;

    private UnitMeasureFilter unit;

    private BigDecimalFilter warehousePrices;

    private BigDecimalFilter unitPrices;

    private BigDecimalFilter sellingPrices;

    private IntegerFilter stock;

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

    public UnitMeasureFilter getUnit() {
        return unit;
    }

    public void setUnit(UnitMeasureFilter unit) {
        this.unit = unit;
    }

    public BigDecimalFilter getWarehousePrices() {
        return warehousePrices;
    }

    public void setWarehousePrices(BigDecimalFilter warehousePrices) {
        this.warehousePrices = warehousePrices;
    }

    public BigDecimalFilter getUnitPrices() {
        return unitPrices;
    }

    public void setUnitPrices(BigDecimalFilter unitPrices) {
        this.unitPrices = unitPrices;
    }

    public BigDecimalFilter getSellingPrices() {
        return sellingPrices;
    }

    public void setSellingPrices(BigDecimalFilter sellingPrices) {
        this.sellingPrices = sellingPrices;
    }

    public IntegerFilter getStock() {
        return stock;
    }

    public void setStock(IntegerFilter stock) {
        this.stock = stock;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(warehousePrices, that.warehousePrices) &&
            Objects.equals(unitPrices, that.unitPrices) &&
            Objects.equals(sellingPrices, that.sellingPrices) &&
            Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        barcode,
        name,
        unit,
        warehousePrices,
        unitPrices,
        sellingPrices,
        stock
        );
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (unit != null ? "unit=" + unit + ", " : "") +
                (warehousePrices != null ? "warehousePrices=" + warehousePrices + ", " : "") +
                (unitPrices != null ? "unitPrices=" + unitPrices + ", " : "") +
                (sellingPrices != null ? "sellingPrices=" + sellingPrices + ", " : "") +
                (stock != null ? "stock=" + stock + ", " : "") +
            "}";
    }

}
