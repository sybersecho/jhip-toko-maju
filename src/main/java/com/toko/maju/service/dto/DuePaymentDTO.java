package com.toko.maju.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the DuePayment entity.
 */
public class DuePaymentDTO implements Serializable {

    private Long id;

    private BigDecimal remainingPayment;

    @NotNull
    private Instant createdDate;

    @NotNull
    private Boolean settled;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal paid;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalPayment;


    private Long creatorId;

    private String creatorLogin;

    private Long saleId;

    private String saleNoInvoice;
    
    private String customerFirstName;
    
    private String customerLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(BigDecimal remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long userId) {
        this.creatorId = userId;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public void setCreatorLogin(String userLogin) {
        this.creatorLogin = userLogin;
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

    public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DuePaymentDTO duePaymentDTO = (DuePaymentDTO) o;
        if (duePaymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), duePaymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DuePaymentDTO{" +
            "id=" + getId() +
            ", remainingPayment=" + getRemainingPayment() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", settled='" + isSettled() + "'" +
            ", paid=" + getPaid() +
            ", totalPayment=" + getTotalPayment() +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            ", sale=" + getSaleId() +
            ", sale='" + getSaleNoInvoice() + "'" +
            "}";
    }
}
