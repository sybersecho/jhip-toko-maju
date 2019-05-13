package com.toko.maju.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.toko.maju.domain.enumeration.ProductStatus;

/**
 * A DTO for the ReturnItem entity.
 */
public class ReturnItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String barcode;

    @NotNull
    private String productName;

    @NotNull
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @NotNull
    private ProductStatus productStatus;


    private Long productId;

    private String productBarcode;

    private Long returnTransactionId;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public Long getReturnTransactionId() {
        return returnTransactionId;
    }

    public void setReturnTransactionId(Long returnTransactionId) {
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

        ReturnItemDTO returnItemDTO = (ReturnItemDTO) o;
        if (returnItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), returnItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReturnItemDTO{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", productStatus='" + getProductStatus() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductBarcode() + "'" +
            ", returnTransaction=" + getReturnTransactionId() +
            "}";
    }
}
