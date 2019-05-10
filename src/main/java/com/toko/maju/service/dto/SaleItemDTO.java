package com.toko.maju.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the SaleItem entity.
 */
public class SaleItemDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer quantity;

    @DecimalMin(value = "0")
    private BigDecimal totalPrice;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal sellingPrice;

    @NotNull
    private String productName;


    private Long saleId;

    private String saleNoInvoice;

    private Long productId;

    private String barcode;

    private String unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleTransactionsId) {
        this.saleId = saleTransactionsId;
    }

    public String getSaleNoInvoice() {
        return saleNoInvoice;
    }

    public void setSaleNoInvoice(String saleTransactionsNoInvoice) {
        this.saleNoInvoice = saleTransactionsNoInvoice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SaleItemDTO saleItemDTO = (SaleItemDTO) o;
        if (saleItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            ", sellingPrice=" + getSellingPrice() +
            ", productName='" + getProductName() + "'" +
            ", sale=" + getSaleId() +
            ", sale='" + getSaleNoInvoice() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductName() + "'" +
            ", Barcode='" + getBarcode() + "'" +
            ", Unit='" + getUnit() + "'" +
            ", SellingPrice='" + getSellingPrice() + "'" +
            "}";
    }
}
