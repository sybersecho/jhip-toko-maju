package com.toko.maju.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the StockOrderRequest entity.
 */
public class StockOrderRequestDTO implements Serializable {

    private Long id;

    private String barcode;

    private String name;

    private String unitName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Instant createdDate;


    private Long stockOrderId;

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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getStockOrderId() {
        return stockOrderId;
    }

    public void setStockOrderId(Long stockOrderId) {
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

        StockOrderRequestDTO stockOrderRequestDTO = (StockOrderRequestDTO) o;
        if (stockOrderRequestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrderRequestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrderRequestDTO{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", name='" + getName() + "'" +
            ", unitName='" + getUnitName() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", stockOrder=" + getStockOrderId() +
            "}";
    }
}
