package com.toko.maju.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.toko.maju.domain.enumeration.TransactionType;

/**
 * A DTO for the ReturnTransaction entity.
 */
public class ReturnTransactionDTO implements Serializable {

    private Long id;

    private Instant created_date;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private BigDecimal totalPriceReturn;

    // @NotNull
    private String noTransaction;

    @NotNull
    private Boolean cashReturned;


    private Long creatorId;

    private String creatorLogin;

    private Long customerId;

    private String customerCode;

    private Long supplierId;

    private String supplierCode;

    private Set<ReturnItemDTO> returnItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Instant created_date) {
        this.created_date = created_date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTotalPriceReturn() {
        return totalPriceReturn;
    }

    public void setTotalPriceReturn(BigDecimal totalPriceReturn) {
        this.totalPriceReturn = totalPriceReturn;
    }

    public String getNoTransaction() {
        return noTransaction;
    }

    public void setNoTransaction(String noTransaction) {
        this.noTransaction = noTransaction;
    }

    public Boolean isCashReturned() {
        return cashReturned;
    }

    public void setCashReturned(Boolean cashReturned) {
        this.cashReturned = cashReturned;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Set<ReturnItemDTO> getReturnItems() {
        return returnItems;
    }

    public void setReturnItems(Set<ReturnItemDTO> returnItems) {
        this.returnItems = returnItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReturnTransactionDTO returnTransactionDTO = (ReturnTransactionDTO) o;
        if (returnTransactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), returnTransactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReturnTransactionDTO{" +
            "id=" + getId() +
            ", created_date='" + getCreated_date() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", totalPriceReturn=" + getTotalPriceReturn() +
            ", noTransaction='" + getNoTransaction() + "'" +
            ", cashReturned='" + isCashReturned() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            ", customer=" + getCustomerId() +
            ", customer='" + getCustomerCode() + "'" +
            ", supplier=" + getSupplierId() +
            ", supplier='" + getSupplierCode() + "'" +
            ", items=" + getReturnItems() +
            "}";
    }
}
