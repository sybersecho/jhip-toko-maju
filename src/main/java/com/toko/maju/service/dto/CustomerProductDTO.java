package com.toko.maju.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the CustomerProduct entity.
 */
public class CustomerProductDTO implements Serializable {

    private Long id;


    private Long productId;

    private String productName;

    private Long customerId;

    private String customerCode;

//    private String supplierName;//TODO
//    @NotNull
    private String barcode;
    
//    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrices; 
    
//    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal sellingPrices;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal specialPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerProductDTO customerProductDTO = (CustomerProductDTO) o;
        if (customerProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerProductDTO{" +
            "id=" + getId() +
            ", specialPrice=" + getSpecialPrice() +
            ", product=" + getProductId() +
            ", product='" + getProductName() + "'" +
            ", customer=" + getCustomerId() +
            ", customer='" + getCustomerCode() + "'" +
            "}";
    }
}
