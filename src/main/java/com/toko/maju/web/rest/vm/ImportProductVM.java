package com.toko.maju.web.rest.vm;

import java.math.BigDecimal;

public class ImportProductVM {
    private String barcode;
    private String productName;
    private BigDecimal warehousePrice;
    private BigDecimal unitPrice;
    private BigDecimal salePrice;
    private String unit;
    private String supplierName;
    private String supplierCode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getWarehousePrice() {
        return warehousePrice;
    }

    public void setWarehousePrice(BigDecimal warehousePrice) {
        this.warehousePrice = warehousePrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setsalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Override
    public String toString() {
        return "ImportProductVM{" +
            "barcode='" + barcode + '\'' +
            ", productName='" + productName + '\'' +
            ", warehousePrice=" + warehousePrice +
            ", unitPrice=" + unitPrice +
            ", salePrice=" + salePrice +
            ", unit='" + unit + '\'' +
            ", supplierName='" + supplierName + '\'' +
            ", supplierCode='" + supplierCode + '\'' +
            '}';
    }
}
