package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "warehouse_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal warehousePrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "selling_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellingPrice;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @JsonIgnoreProperties("suppliers")
    private Supplier supplier;

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

    public BigDecimal getWarehousePrice() {
        return warehousePrice;
    }

    public Product warehousePrice(BigDecimal warehousePrice) {
        this.warehousePrice = warehousePrice;
        return this;
    }

    public void setWarehousePrice(BigDecimal warehousePrice) {
        this.warehousePrice = warehousePrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Product unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public Product sellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public Product supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
            ", warehousePrice=" + getWarehousePrice() +
            ", unitPrice=" + getUnitPrice() +
            ", sellingPrice=" + getSellingPrice() +
            ", stock=" + getStock() +
            "}";
    }
}
