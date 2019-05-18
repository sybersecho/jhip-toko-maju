package com.toko.maju.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockOrderProcess entity.
 */
public class StockOrderProcessDTO implements Serializable {

    private Long id;

    private String barcode;

    private String name;

    private Integer quantityRequest;

    private Integer stockInHand;

    private Integer quantityApprove;

    private Instant createdDate;


    private Long creatorId;

    private String creatorLogin;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityRequest() {
        return quantityRequest;
    }

    public void setQuantityRequest(Integer quantityRequest) {
        this.quantityRequest = quantityRequest;
    }

    public Integer getStockInHand() {
        return stockInHand;
    }

    public void setStockInHand(Integer stockInHand) {
        this.stockInHand = stockInHand;
    }

    public Integer getQuantityApprove() {
        return quantityApprove;
    }

    public void setQuantityApprove(Integer quantityApprove) {
        this.quantityApprove = quantityApprove;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOrderProcessDTO stockOrderProcessDTO = (StockOrderProcessDTO) o;
        if (stockOrderProcessDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrderProcessDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrderProcessDTO{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", name='" + getName() + "'" +
            ", quantityRequest=" + getQuantityRequest() +
            ", stockInHand=" + getStockInHand() +
            ", quantityApprove=" + getQuantityApprove() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            "}";
    }
}
