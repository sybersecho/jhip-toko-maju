package com.toko.maju.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ProjectProduct entity.
 */
public class ProjectProductDTO implements Serializable {

	private Long id;

	@NotNull
	@DecimalMin(value = "0")
	private BigDecimal specialPrice;

	private Long productId;

	private String productName;

	private Long projectId;

	private String projectName;

	private String barcode;

	@DecimalMin(value = "0")
	private BigDecimal unitPrice;

	@DecimalMin(value = "0")
	private BigDecimal sellingPrice;

	private String unit;

	private String supplierCode;

	private String supplierName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ProjectProductDTO projectProductDTO = (ProjectProductDTO) o;
		if (projectProductDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), projectProductDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "ProjectProductDTO{" + "id=" + getId() + ", specialPrice=" + getSpecialPrice() + ", product="
				+ getProductId() + ", product='" + getProductName() + "'" + ", project=" + getProjectId()
				+ ", project='" + getProjectName() + "'" + "}";
	}
}
