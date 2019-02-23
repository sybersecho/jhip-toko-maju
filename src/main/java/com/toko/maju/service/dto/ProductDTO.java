package com.toko.maju.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.toko.maju.domain.enumeration.UnitMeasure;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String barcode;

    @NotNull
    private String name;

    @NotNull
    private UnitMeasure unit;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal warehousePrices;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrices;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal sellingPrices;

    @NotNull
    private Integer stock;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitMeasure getUnit() {
        return unit;
    }

    public void setUnit(UnitMeasure unit) {
        this.unit = unit;
    }

    public BigDecimal getWarehousePrices() {
        return warehousePrices;
    }

    public void setWarehousePrices(BigDecimal warehousePrices) {
        this.warehousePrices = warehousePrices;
    }

    public BigDecimal getUnitPrices() {
        return unitPrices;
    }

    public void setUnitPrices(BigDecimal unitPrices) {
        this.unitPrices = unitPrices;
    }

    public BigDecimal getSellingPrices() {
        return sellingPrices;
    }

    public void setSellingPrices(BigDecimal sellingPrices) {
        this.sellingPrices = sellingPrices;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
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

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", warehousePrices=" + getWarehousePrices() +
            ", unitPrices=" + getUnitPrices() +
            ", sellingPrices=" + getSellingPrices() +
            ", stock=" + getStock() +
            "}";
    }
}
