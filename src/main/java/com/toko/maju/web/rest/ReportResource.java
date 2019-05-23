package com.toko.maju.web.rest;


import com.toko.maju.generator.ExcelGenerator;
import com.toko.maju.service.DuePaymentService;
import com.toko.maju.service.SaleTransactionsQueryService;
import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.web.rest.vm.ReportPaymentDetailVM;
import com.toko.maju.web.rest.vm.ReportPaymentVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private final SaleTransactionsQueryService saleTransactionsQueryService;
    private final DuePaymentService duePaymentService;

    public ReportResource(SaleTransactionsQueryService saleTransactionsQueryService, DuePaymentService duePaymentService) {
        this.saleTransactionsQueryService = saleTransactionsQueryService;
        this.duePaymentService = duePaymentService;
    }

    @GetMapping("/report/payment")
    public ResponseEntity<List<ReportPaymentVM>> getReportPayment(SaleTransactionsCriteria criteria) {
        log.debug("REST request to get Payment Report by criteria: {}", criteria);

        List<SaleTransactionsDTO> sales = saleTransactionsQueryService.findByCriteria(criteria);
        Set<Long> saleIds = new HashSet<>();
        Set<String> saleInvoices = new HashSet<>();
        this.createSaleInfo(sales, saleIds, saleInvoices);
        List<DuePaymentDTO> paymentDTOS = duePaymentService.findBySaleIds(saleIds);
        HashMap<String, List<DuePaymentDTO>> paymentMap = this.createPaymentMap(saleInvoices, paymentDTOS);
        List<ReportPaymentVM> payments = this.createReportPayment(sales, paymentMap);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/report/extract-report-payment")
    public ResponseEntity<InputStreamResource> extractReportPayment(SaleTransactionsCriteria criteria) {
        log.debug("REST request to get Payment Report by criteria: {}", criteria);

        List<SaleTransactionsDTO> sales = saleTransactionsQueryService.findByCriteria(criteria);
        Set<Long> saleIds = new HashSet<>();
        Set<String> saleInvoices = new HashSet<>();
        this.createSaleInfo(sales, saleIds, saleInvoices);
        List<DuePaymentDTO> paymentDTOS = duePaymentService.findBySaleIds(saleIds);
        HashMap<String, List<DuePaymentDTO>> paymentMap = this.createPaymentMap(saleInvoices, paymentDTOS);
        List<ReportPaymentVM> payments = this.createReportPayment(sales, paymentMap);

        ByteArrayInputStream in = ExcelGenerator.reportPayment(payments);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sale-report-detail.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(new InputStreamResource(in));
    }

    private void createSaleInfo(List<SaleTransactionsDTO> sales, Set<Long> saleIds, Set<String> saleInvoice) {
        sales.forEach(sale -> {
            saleIds.add(sale.getId());
            saleInvoice.add(sale.getNoInvoice());
        });
    }

    private HashMap<String, List<DuePaymentDTO>> createPaymentMap(Set<String> saleInvoice, List<DuePaymentDTO> paymentDTOS) {
        HashMap<String, List<DuePaymentDTO>> paymentMap = new HashMap<>();
        saleInvoice.forEach(noInvoice -> paymentMap.put(noInvoice, this.getPaymentFrom(noInvoice, paymentDTOS)));
        return paymentMap;
    }


    private List<DuePaymentDTO> getPaymentFrom(String noInvoice, List<DuePaymentDTO> paymentDTOS) {
        List<DuePaymentDTO> dtos = paymentDTOS.stream().filter(payment -> payment.getSaleNoInvoice() == noInvoice).collect(Collectors.toList());
        return dtos;
    }

    private List<ReportPaymentVM> createReportPayment(List<SaleTransactionsDTO> sales, HashMap<String, List<DuePaymentDTO>> paymentMap) {
        List<ReportPaymentVM> reports = new ArrayList<>();
        sales.forEach(sale -> reports.add(this.addReport(sale, paymentMap.get(sale.getNoInvoice()))));
        return reports;
    }

    private ReportPaymentVM addReport(SaleTransactionsDTO sale, List<DuePaymentDTO> payments) {
        ReportPaymentVM report = new ReportPaymentVM();
        report.setCustomer(sale.getCustomerFullName());
        report.setNoInvoice(sale.getNoInvoice());
        report.setProject(sale.getProjectName());
        report.setSaleDate(sale.getSaleDate());
        report.setTotalPayment(sale.getTotalPayment());
        report.setPaymentDetails(this.createPaymentDetails(payments));
        return report;
    }

    private List<ReportPaymentDetailVM> createPaymentDetails(List<DuePaymentDTO> payments) {
        List<ReportPaymentDetailVM> reportPaymentDetail = new ArrayList<>();
        payments.forEach(payment -> {
            ReportPaymentDetailVM paymentDetail = new ReportPaymentDetailVM();
            paymentDetail.setPaid(payment.getPaid());
            paymentDetail.setPaymentDate(payment.getCreatedDate());
            paymentDetail.setRemainingPayment(payment.getRemainingPayment());

            reportPaymentDetail.add(paymentDetail);
        });
        return reportPaymentDetail;
    }
}
