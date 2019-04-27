package com.toko.maju.web.rest.vm;

import java.io.Serializable;
import java.util.List;

import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.dto.SaleTransactionsDTO;

public class DuePaymentVM implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DuePaymentDTO> duePayments;
	private List<SaleTransactionsDTO> sales;

	public List<DuePaymentDTO> getDuePayments() {
		return duePayments;
	}

	public void setDuePayments(List<DuePaymentDTO> duePayments) {
		this.duePayments = duePayments;
	}

	public List<SaleTransactionsDTO> getSales() {
		return sales;
	}

	public void setSales(List<SaleTransactionsDTO> sales) {
		this.sales = sales;
	}

	@Override
	public String toString() {
		return "DuePaymentVM [duePayments=" + duePayments + ", sales=" + sales + "]";
	}

}
