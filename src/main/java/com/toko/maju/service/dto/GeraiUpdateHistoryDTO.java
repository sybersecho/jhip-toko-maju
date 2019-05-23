package com.toko.maju.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GeraiUpdateHistory entity.
 */
public class GeraiUpdateHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long lastSaleId;

    @NotNull
    private Instant createdDate;

    @NotNull
    private Instant saleDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastSaleId() {
        return lastSaleId;
    }

    public void setLastSaleId(Long lastSaleId) {
        this.lastSaleId = lastSaleId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = (GeraiUpdateHistoryDTO) o;
        if (geraiUpdateHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geraiUpdateHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeraiUpdateHistoryDTO{" +
            "id=" + getId() +
            ", lastSaleId=" + getLastSaleId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", saleDate='" + getSaleDate() + "'" +
            "}";
    }
}
