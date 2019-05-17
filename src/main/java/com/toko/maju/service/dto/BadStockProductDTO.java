package com.toko.maju.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BadStockProduct entity.
 */
public class BadStockProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String barcode;

    @NotNull
    private String productName;

    @NotNull
    @Min(value = 0)
    private Integer quantity;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BadStockProductDTO badStockProductDTO = (BadStockProductDTO) o;
        if (badStockProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), badStockProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BadStockProductDTO{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
