package com.toko.maju.service.util;

import javax.servlet.http.HttpServletRequest;

import com.toko.maju.service.dto.SaleTransactionsDTO;

public class SessionUtil {
	private static final String SALE_SESSION = "sale";

	public static SaleTransactionsDTO getSaleInSession(HttpServletRequest request) {
		SaleTransactionsDTO sale = (SaleTransactionsDTO) request.getSession().getAttribute(SALE_SESSION);
		if (sale == null) {
			sale = new SaleTransactionsDTO();

			request.getSession().setAttribute(SALE_SESSION, sale);
		}

		return sale;
	}

	public static SaleTransactionsDTO addSaleIntoSession(HttpServletRequest request, SaleTransactionsDTO sale) {
		request.getSession().setAttribute(SALE_SESSION, sale);

		return sale;
	}

	public static void removeSaleInSession(HttpServletRequest reqeust) {
		reqeust.getSession().removeAttribute(SALE_SESSION);
	}
}
