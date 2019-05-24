package com.toko.maju.web.rest;

import com.toko.maju.domain.GeraiConfig;
import com.toko.maju.domain.GeraiTransaction;
import com.toko.maju.domain.GeraiUpdateHistory;
import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.repository.search.GeraiConfigSearchRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.service.*;
import com.toko.maju.service.dto.*;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.response.GeraiResponse;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GeraiTransaction.
 */
@RestController
@RequestMapping("/api")
public class GeraiTransactionResource {

    private final Logger log = LoggerFactory.getLogger(GeraiTransactionResource.class);

    private static final String ENTITY_NAME = "geraiTransaction";

    private final GeraiTransactionService geraiTransactionService;

    private final GeraiTransactionQueryService geraiTransactionQueryService;

    @Autowired
    private final GeraiService geraiService = null;

    @Autowired
    private final GeraiConfigService geraiConfigService = null;

    @Autowired
    private final SaleTransactionsQueryService saleTransactionsQueryService = null;

    @Autowired
    private final ProductService productService = null;

    @Autowired
    private final GeraiUpdateHistoryQueryService geraiUpdateHistoryQueryService = null;

    @Autowired
    private final GeraiUpdateHistoryService geraiUpdateHistoryService = null;

    public GeraiTransactionResource(GeraiTransactionService geraiTransactionService, GeraiTransactionQueryService geraiTransactionQueryService) {
        this.geraiTransactionService = geraiTransactionService;
        this.geraiTransactionQueryService = geraiTransactionQueryService;
    }

    /**
     * POST  /gerai-transactions : Create a new geraiTransaction.
     *
     * @param geraiTransactionDTO the geraiTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geraiTransactionDTO, or with status 400 (Bad Request) if the geraiTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gerai-transactions")
    public ResponseEntity<GeraiTransactionDTO> createGeraiTransaction(@Valid @RequestBody GeraiTransactionDTO geraiTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save GeraiTransaction : {}", geraiTransactionDTO);
        if (geraiTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new geraiTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeraiTransactionDTO result = geraiTransactionService.save(geraiTransactionDTO);
        return ResponseEntity.created(new URI("/api/gerai-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gerai-transactions : Updates an existing geraiTransaction.
     *
     * @param geraiTransactionDTO the geraiTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geraiTransactionDTO,
     * or with status 400 (Bad Request) if the geraiTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the geraiTransactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gerai-transactions")
    public ResponseEntity<GeraiTransactionDTO> updateGeraiTransaction(@Valid @RequestBody GeraiTransactionDTO geraiTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update GeraiTransaction : {}", geraiTransactionDTO);
        if (geraiTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeraiTransactionDTO result = geraiTransactionService.save(geraiTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, geraiTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gerai-transactions : get all the geraiTransactions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of geraiTransactions in body
     */
    @GetMapping("/gerai-transactions")
    public ResponseEntity<List<GeraiTransactionDTO>> getAllGeraiTransactions(GeraiTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GeraiTransactions by criteria: {}", criteria);
        Page<GeraiTransactionDTO> page = geraiTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gerai-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /gerai-transactions/count : count all the geraiTransactions.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/gerai-transactions/count")
    public ResponseEntity<Long> countGeraiTransactions(GeraiTransactionCriteria criteria) {
        log.debug("REST request to count GeraiTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(geraiTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /gerai-transactions/:id : get the "id" geraiTransaction.
     *
     * @param id the id of the geraiTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geraiTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gerai-transactions/{id}")
    public ResponseEntity<GeraiTransactionDTO> getGeraiTransaction(@PathVariable Long id) {
        log.debug("REST request to get GeraiTransaction : {}", id);
        Optional<GeraiTransactionDTO> geraiTransactionDTO = geraiTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geraiTransactionDTO);
    }

    /**
     * DELETE  /gerai-transactions/:id : delete the "id" geraiTransaction.
     *
     * @param id the id of the geraiTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gerai-transactions/{id}")
    public ResponseEntity<Void> deleteGeraiTransaction(@PathVariable Long id) {
        log.debug("REST request to delete GeraiTransaction : {}", id);
        geraiTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/gerai-transactions?query=:query : search for the geraiTransaction corresponding
     * to the query.
     *
     * @param query    the query of the geraiTransaction search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/gerai-transactions")
    public ResponseEntity<List<GeraiTransactionDTO>> searchGeraiTransactions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GeraiTransactions for query {}", query);
        Page<GeraiTransactionDTO> page = geraiTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gerai-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/gerai-transactions/{geraiCode}/history")
    public ResponseEntity<GeraiResponse> saveGeraiTransactionHistory(@PathVariable String geraiCode, @Valid @RequestBody List<GeraiTransactionDTO> geraiTransactionDTOS) {
        log.debug("REST request to save GeraiTransaction : {}", geraiCode);
        try {
            Optional<GeraiDTO> gerai = geraiService.findByCode(geraiCode);

            if (!gerai.isPresent() || geraiTransactionDTOS.isEmpty()) {
                return ResponseEntity.badRequest().body(GeraiResponse.FailResponse());
            }

            List<GeraiTransactionDTO> geraiTransactions = geraiTransactionService.saveAll(geraiTransactionDTOS);
            if (geraiTransactions.isEmpty()) {
                return ResponseEntity.badRequest().body(GeraiResponse.FailResponse());
            }

            log.debug("success!!!!");
            return ResponseEntity.ok(GeraiResponse.SuccessResponse());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(GeraiResponse.ErrorResponse());
        }
    }

    @Scheduled(cron = "*/60 * * * * ?")
    public void sendStatus() {
        GeraiConfigDTO config = geraiConfigService.findOne(1L).get();
        if (config != null && !config.isActivated()) {
            log.debug("not active");
            return;
        }

        try {
            // get latest id
            GeraiUpdateHistoryDTO latest = geraiUpdateHistoryService.findLatest();
//            Long latestHistoryId = (latest != null && latest.getId() != null) ? latest.getId() : 1L;
//            //create criteria
//            LongFilter saleIdFilter = new LongFilter();
//            saleIdFilter.setGreaterThan(latestHistoryId);
//            SaleTransactionsCriteria criteria = new SaleTransactionsCriteria();
//            criteria.setId(saleIdFilter);

            Instant latestSaleDateHistory = (latest != null && latest.getSaleDate() != null) ? latest.getSaleDate() : Instant.EPOCH;
            log.debug("latest date: {}", latestSaleDateHistory.toString());
            InstantFilter saleDateFilter = new InstantFilter();
            saleDateFilter.setGreaterThan(latestSaleDateHistory);
            SaleTransactionsCriteria criteria = new SaleTransactionsCriteria();
            criteria.setSaleDate(saleDateFilter);

            List<SaleTransactionsDTO> sales = saleTransactionsQueryService.findByCriteria(criteria);
            if (sales.isEmpty()) {
                log.debug("no transactions recently");
                return;
            }
            //create transaction from sales
            List<GeraiTransactionDTO> dtos = new ArrayList<>();
            for (SaleTransactionsDTO sale : sales) {
                sale.getItems().forEach(item -> dtos.add(createGerTransaction(item, config)));
            }

            // build url
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(config.getUrlToko());
            urlBuilder.append("/api/gerai-transactions/");
            urlBuilder.append(config.getCodeGerai());
            urlBuilder.append("/history");
            log.debug("URL: {}", urlBuilder.toString());

            // hit toko
            RestTemplate restTemplate = new RestTemplate();
            GeraiResponse response = restTemplate.postForObject(urlBuilder.toString(), dtos, GeraiResponse.class);
            if (response.getStatus() == GeraiResponse.SUCCESS_STATUS) {
                Long latestSaleId = sales.get(sales.size() - 1).getId();
                Instant latestSaleDate = sales.get(sales.size() - 1).getSaleDate();
                GeraiUpdateHistoryDTO updateStatus = new GeraiUpdateHistoryDTO();
                updateStatus.setCreatedDate(Instant.now());
                updateStatus.setLastSaleId(latestSaleId + 1);
                updateStatus.setSaleDate(latestSaleDate);
                geraiUpdateHistoryService.save(updateStatus);
            }
            log.debug("response: {}", response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private GeraiTransactionDTO createGerTransaction(SaleItemDTO item, GeraiConfigDTO config) {
        GeraiTransactionDTO dto = new GeraiTransactionDTO();
        Optional<ProductDTO> product = productService.findOne(item.getProductId());
        dto.setName(item.getProductName());
        dto.setCurrentStock(product.get().getStock());
        dto.setGeraiName(config.getNameGerai());
        dto.setQuantity(item.getQuantity());
        dto.setReceivedDate(Instant.now());
        dto.setGeraiId(1L);
        dto.setGeraiCode(config.getCodeGerai());
        dto.setBarcode(item.getBarcode());
        return dto;
    }

}
