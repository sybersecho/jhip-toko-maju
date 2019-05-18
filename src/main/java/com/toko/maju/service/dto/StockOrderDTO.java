package com.toko.maju.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockOrder entity.
 */
public class StockOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private String siteLocation;

    @NotNull
    private Instant createdDate;

    @NotNull
    private Boolean processed;

    private Instant processedDate;


    private Long creatorId;

    private String creatorLogin;

    private Long approvalId;

    private String approvalLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Instant getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
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

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long userId) {
        this.approvalId = userId;
    }

    public String getApprovalLogin() {
        return approvalLogin;
    }

    public void setApprovalLogin(String userLogin) {
        this.approvalLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOrderDTO stockOrderDTO = (StockOrderDTO) o;
        if (stockOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrderDTO{" +
            "id=" + getId() +
            ", siteLocation='" + getSiteLocation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", processed='" + isProcessed() + "'" +
            ", processedDate='" + getProcessedDate() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            ", approval=" + getApprovalId() +
            ", approval='" + getApprovalLogin() + "'" +
            "}";
    }
}
