package com.toko.maju.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the SaleTransactions entity.
 */
public class SaleTransactionsDTO implements Serializable {

    private Long id;

    private String noInvoice;

    @DecimalMin(value = "0")
    private BigDecimal discount;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalPayment;

    @DecimalMin(value = "0")
    private BigDecimal remainingPayment;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal paid;

    private Instant saleDate;


    private Long customerId;

    private String customerFirstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoInvoice() {
        return noInvoice;
    }

    public void setNoInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(BigDecimal remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
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

        SaleTransactionsDTO saleTransactionsDTO = (SaleTransactionsDTO) o;
        if (saleTransactionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleTransactionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleTransactionsDTO{" +
            "id=" + getId() +
            ", noInvoice='" + getNoInvoice() + "'" +
            ", discount=" + getDiscount() +
            ", totalPayment=" + getTotalPayment() +
            ", remainingPayment=" + getRemainingPayment() +
            ", paid=" + getPaid() +
            ", saleDate='" + getSaleDate() + "'" +
            ", customer=" + getCustomerId() +
            ", customer='" + getCustomerFirstName() + "'" +
            "}";
    }
}
