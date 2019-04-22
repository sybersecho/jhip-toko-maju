package com.toko.maju.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

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

    @NotNull
    private Boolean settled;


    private Long customerId;

    private String customerFirstName;

    private String customerLastName;

	private String customerCode;

	private String customerAddress;

	private Set<SaleItemDTO> items = new HashSet<SaleItemDTO>();


    private Long creatorId;

    private String creatorLogin;

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

    public Boolean isSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
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

    public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerFullName() {
		StringBuilder buildName = new StringBuilder();
		buildName.append(getCustomerFirstName());
		buildName.append(" ");
		buildName.append(getCustomerLastName());

		return buildName.toString();
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public Set<SaleItemDTO> getItems() {
		return items;
	}

	public void setItems(Set<SaleItemDTO> items) {
		this.items = items;
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
            ", settled='" + isSettled() + "'" +
            ", customer=" + getCustomerId() +
            ", customerCode='" + getCustomerCode() + "'" +
            ", customer='" + getCustomerFirstName() + "'" +
            ", customer='" + getCustomerLastName() + "'" +
            ", customer='" + getCustomerAddress() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            ", items='" + getItems() + "'" +
            "}";
    }
}
