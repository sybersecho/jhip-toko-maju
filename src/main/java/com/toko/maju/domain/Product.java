package com.toko.maju.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.toko.maju.domain.enumeration.UnitMeasure;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private UnitMeasure unit;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "warehouse_prices", precision = 10, scale = 2, nullable = false)
    private BigDecimal warehousePrices;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_prices", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrices;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "selling_prices", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellingPrices;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public Product barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitMeasure getUnit() {
        return unit;
    }

    public Product unit(UnitMeasure unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(UnitMeasure unit) {
        this.unit = unit;
    }

    public BigDecimal getWarehousePrices() {
        return warehousePrices;
    }

    public Product warehousePrices(BigDecimal warehousePrices) {
        this.warehousePrices = warehousePrices;
        return this;
    }

    public void setWarehousePrices(BigDecimal warehousePrices) {
        this.warehousePrices = warehousePrices;
    }

    public BigDecimal getUnitPrices() {
        return unitPrices;
    }

    public Product unitPrices(BigDecimal unitPrices) {
        this.unitPrices = unitPrices;
        return this;
    }

    public void setUnitPrices(BigDecimal unitPrices) {
        this.unitPrices = unitPrices;
    }

    public BigDecimal getSellingPrices() {
        return sellingPrices;
    }

    public Product sellingPrices(BigDecimal sellingPrices) {
        this.sellingPrices = sellingPrices;
        return this;
    }

    public void setSellingPrices(BigDecimal sellingPrices) {
        this.sellingPrices = sellingPrices;
    }

    public Integer getStock() {
        return stock;
    }

    public Product stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
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
