package com.toko.maju.web.rest;

import com.toko.maju.generator.ExcelGenerator;
import com.toko.maju.service.SaleTransactionsQueryService;
import com.toko.maju.service.SaleTransactionsService;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ExportResource {

    private final Logger log = LoggerFactory.getLogger(ExportResource.class);

    private final SaleTransactionsService saleTransactionsService;

    private final SaleTransactionsQueryService saleTransactionsQueryService;

    public ExportResource(SaleTransactionsService saleTransactionsService, SaleTransactionsQueryService saleTransactionsQueryService) {
        this.saleTransactionsService = saleTransactionsService;
        this.saleTransactionsQueryService = saleTransactionsQueryService;
    }

    @GetMapping(value = "/report/sale-report-detail")
    public ResponseEntity<InputStreamResource> extractSaleReportDetail(SaleTransactionsCriteria criteria,
                                                                       Pageable pageable) throws IOException {

        log.debug("REST request to get SaleTransactions by criteria: {}", criteria);
        Page<SaleTransactionsDTO> page = saleTransactionsQueryService.findByCriteria(criteria, pageable);
        List<SaleTransactionsDTO> result = page.getContent();
        log.debug("result: {}", result);

        ByteArrayInputStream in = ExcelGenerator.saleReportDetail(result);
        // return IOUtils.toByteArray(in);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sale-report-detail.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(new InputStreamResource(in));

    }
}
