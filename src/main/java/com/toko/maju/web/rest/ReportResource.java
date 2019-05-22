package com.toko.maju.web.rest;


import com.toko.maju.service.DuePaymentService;
import com.toko.maju.service.SaleTransactionsQueryService;
import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.web.rest.vm.ReportPaymentDetailVM;
import com.toko.maju.web.rest.vm.ReportPaymentVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//        this.createSaleIds(sales, saleIds);
        this.createSaleInfo(sales, saleIds, saleInvoices);
//        log.debug("saleID: {}", saleIds);
//        log.debug("noInvoices: {}", saleInvoices);
        List<DuePaymentDTO> paymentDTOS = duePaymentService.findBySaleIds(saleIds);
//        List<String>
        HashMap<String, List<DuePaymentDTO>> paymentMap = this.createPaymentMap(saleInvoices, paymentDTOS);
//        log.debug("paymentDtos: {}", paymentDTOS);
        List<ReportPaymentVM> payments = this.createReportPayment(sales, paymentMap);

//        log.debug("report payments: {}", payments);
        return ResponseEntity.ok(payments);
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
//        log.debug("payment dtos before: size: {}, values {}", paymentDTOS.size(), paymentDTOS);
        List<DuePaymentDTO> dtos = paymentDTOS.stream().filter(payment -> payment.getSaleNoInvoice() == noInvoice).collect(Collectors.toList());
//        log.debug("payment dtos after: size: {}, values {}", paymentDTOS.size(), paymentDTOS);
//        log.debug("dtos: size: {}, value: {}", dtos.size(), dtos);
        return dtos;
    }

    private List<ReportPaymentVM> createReportPayment(List<SaleTransactionsDTO> sales, HashMap<String, List<DuePaymentDTO>> paymentMap) {
        List<ReportPaymentVM> reports = new ArrayList<>();
        sales.forEach(sale -> reports.add(this.addReport(sale, paymentMap.get(sale.getNoInvoice()))));
//        log.debug("reports: Size: {}; values: {}", reports.size(), reports);
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
//        log.debug("Payment Detail: size: {}, values: {}", payments.size(), payments);
        return reportPaymentDetail;
    }

//    private void createSaleIds(List<SaleTransactionsDTO> sales, Set<Long> saleIds) {
//        sales.forEach(sale -> saleIds.add(sale.getId()));
//    }
}
