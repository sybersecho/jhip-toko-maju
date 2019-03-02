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

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal specialPrice;


    private Long customerId;

    private String customerFirstName;

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
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
            ", customer=" + getCustomerId() +
            ", customer='" + getCustomerFirstName() + "'" +
            "}";
    }
}
