package com.toko.maju.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GeraiTransaction entity.
 */
public class GeraiTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private String barcode;

    @NotNull
    private String name;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer currentStock;

    private Instant receivedDate;


    private Long geraiId;

    private String geraiName;

    private String geraiCode;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Long getGeraiId() {
        return geraiId;
    }

    public void setGeraiId(Long geraiId) {
        this.geraiId = geraiId;
    }

    public String getGeraiName() {
        return geraiName;
    }

    public void setGeraiName(String geraiName) {
        this.geraiName = geraiName;
    }

    public String getGeraiCode() {
        return geraiCode;
    }

    public void setGeraiCode(String geraiCode) {
        this.geraiCode = geraiCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeraiTransactionDTO geraiTransactionDTO = (GeraiTransactionDTO) o;
        if (geraiTransactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geraiTransactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeraiTransactionDTO{" +
            "id=" + id +
            ", barcode='" + barcode + '\'' +
            ", name='" + name + '\'' +
            ", quantity=" + quantity +
            ", currentStock=" + currentStock +
            ", receivedDate=" + receivedDate +
            ", geraiId=" + geraiId +
            ", geraiName='" + geraiName + '\'' +
            ", geraiCode='" + geraiCode + '\'' +
            '}';
    }
}
