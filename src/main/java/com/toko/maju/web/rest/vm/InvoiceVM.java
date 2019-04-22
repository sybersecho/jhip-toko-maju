package com.toko.maju.web.rest.vm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class InvoiceVM implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String noInvoice;
	private String customer;
	private BigDecimal totalPayment;
	private BigDecimal remainingPayment;
	private BigDecimal paid;
	private Instant saleDate;
	private String creator;

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

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Instant getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Instant saleDate) {
		this.saleDate = saleDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "InvoiceVM [id=" + id + ", noInvoice=" + noInvoice + ", customer=" + customer + ", totalPayment="
				+ totalPayment + ", remainingPayment=" + remainingPayment + ", paid=" + paid + ", saleDate=" + saleDate
				+ ", creator=" + creator + "]";
	}

}
