package com.toko.maju.web.rest.vm;

import com.toko.maju.domain.Unit;
import com.toko.maju.domain.enumeration.UnitMeasure;

import java.math.BigDecimal;

public class ExtractProductVM {
    private String barcode;
    private String productName;
    private Unit unit;
    private BigDecimal unitPrice;
    private BigDecimal salePrice;
    private String supplierCode;
    private String supplierName;
    private String supplierAddress;
    private String supplierNoTelp;

    public ExtractProductVM() {
    }

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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierNoTelp() {
        return supplierNoTelp;
    }

    public void setSupplierNoTelp(String supplierNoTelp) {
        this.supplierNoTelp = supplierNoTelp;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ExtractProductVM{" +
            "barcode='" + barcode + '\'' +
            ", productName='" + productName + '\'' +
            ", unit=" + unit +
            ", unitPrice=" + unitPrice +
            ", salePrice=" + salePrice +
            ", supplierCode='" + supplierCode + '\'' +
            ", supplierName='" + supplierName + '\'' +
            ", supplierAddress='" + supplierAddress + '\'' +
            ", supplierNoTelp='" + supplierNoTelp + '\'' +
            '}';
    }
}
