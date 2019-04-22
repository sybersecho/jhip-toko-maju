package com.toko.maju.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toko.maju.service.SaleTransactionsQueryService;
import com.toko.maju.service.SaleTransactionsService;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.web.rest.vm.InvoiceVM;

@RestController
@RequestMapping("/api")
public class InvoiceResource {
	private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

	private static final String ENTITY_NAME = "saleTransactions";

	private final SaleTransactionsService saleTransactionsService;

	private final SaleTransactionsQueryService saleTransactionsQueryService;

	public InvoiceResource(SaleTransactionsService saleTransactionsService,
			SaleTransactionsQueryService saleTransactionsQueryService) {
		this.saleTransactionsService = saleTransactionsService;
		this.saleTransactionsQueryService = saleTransactionsQueryService;
	}

	/**
	 * GET /sale-transactions : get all the saleTransactions.
	 *
	 * @param pageable the pagination information
	 * @param criteria the criterias which the requested entities should match
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         saleTransactions in body
	 */
	@GetMapping("/invoices")
	public ResponseEntity<List<InvoiceVM>> getAllSaleTransactions(SaleTransactionsCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get Invoice by criteria: {}", criteria);
		Page<SaleTransactionsDTO> page = saleTransactionsQueryService.findByCriteria(criteria, pageable);
		List<InvoiceVM> listInvoice = createInvoiceFromSale(page);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoices");
		return ResponseEntity.ok().headers(headers).body(listInvoice);
	}

	private List<InvoiceVM> createInvoiceFromSale(Page<SaleTransactionsDTO> page) {
		List<InvoiceVM> list = new ArrayList<InvoiceVM>();
		page.getContent().forEach(sale -> convertSaleToInvoice(sale, list));
		return list;
	}

	private void convertSaleToInvoice(SaleTransactionsDTO sale, List<InvoiceVM> list) {
		InvoiceVM item = new InvoiceVM();
		item.setId(sale.getId());
		item.setNoInvoice(sale.getNoInvoice());
		item.setPaid(sale.getPaid());
		item.setRemainingPayment(sale.getRemainingPayment());
		item.setTotalPayment(sale.getTotalPayment());
		item.setCustomer(sale.getCustomerFullName());
		item.setSaleDate(sale.getSaleDate());
		item.setCreator(sale.getCreatorLogin());

		list.add(item);
//		return null;
	}

//	private ResponseEntity<List<InvoiceVM>> converToInvoice() {
//		// TODO Auto-generated method stub
//		List<InvoiceVM> list = new ArrayList<InvoiceVM>();
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}
}
