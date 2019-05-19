package com.toko.maju.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Purchase entity.
 */
public class PurchaseDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalPayment;

    @NotNull
    private Instant createdDate;

    private String note;

    private List<PurchaseListDTO> purchaseLists = new ArrayList<>();

    private Long supplierId;

    private String supplierName;

    private Long creatorId;

    private String creatorLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public List<PurchaseListDTO> getPurchaseLists() {
        return purchaseLists;
    }

    public void setPurchaseLists(List<PurchaseListDTO> purchaseLists) {
        this.purchaseLists = purchaseLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseDTO purchaseDTO = (PurchaseDTO) o;
        if (purchaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseDTO{" +
            "id=" + getId() +
            ", totalPayment=" + getTotalPayment() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", note='" + getNote() + "'" +
            ", supplier=" + getSupplierId() +
            ", supplier='" + getSupplierName() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            ", list='" + getPurchaseLists() + "'" +
            "}";
    }
}
