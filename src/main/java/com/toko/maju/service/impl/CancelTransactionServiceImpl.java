package com.toko.maju.service.impl;

import com.toko.maju.domain.*;
import com.toko.maju.domain.enumeration.StatusTransaction;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.SaleTransactionsRepository;
import com.toko.maju.repository.SequenceNumberRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.repository.search.SequenceNumberSearchRepository;
import com.toko.maju.service.CancelTransactionService;
import com.toko.maju.repository.CancelTransactionRepository;
import com.toko.maju.repository.search.CancelTransactionSearchRepository;
import com.toko.maju.service.ProductQueryService;
import com.toko.maju.service.SaleTransactionsQueryService;
import com.toko.maju.service.dto.CancelTransactionDTO;
import com.toko.maju.service.mapper.CancelTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CancelTransaction.
 */
@Service
@Transactional
public class CancelTransactionServiceImpl implements CancelTransactionService {

    private final Logger log = LoggerFactory.getLogger(CancelTransactionServiceImpl.class);

    private final CancelTransactionRepository cancelTransactionRepository;

    private final CancelTransactionMapper cancelTransactionMapper;

    @Autowired
    private final SaleTransactionsRepository saleTransactionsRepository = null;

    @Autowired
    private final SaleTransactionsSearchRepository saleTransactionsSearchRepository = null;

    @Autowired
    private final ProductRepository productRepository = null;

    @Autowired
    private  final ProductSearchRepository productSearchRepository = null;

    @Autowired
    private final SequenceNumberRepository sequenceNumberRepository = null;

    @Autowired
    private final SequenceNumberSearchRepository sequenceNumberSearchRepository = null;

    private final CancelTransactionSearchRepository cancelTransactionSearchRepository;

    public CancelTransactionServiceImpl(CancelTransactionRepository cancelTransactionRepository, CancelTransactionMapper cancelTransactionMapper, CancelTransactionSearchRepository cancelTransactionSearchRepository) {
        this.cancelTransactionRepository = cancelTransactionRepository;
        this.cancelTransactionMapper = cancelTransactionMapper;
        this.cancelTransactionSearchRepository = cancelTransactionSearchRepository;
    }

    /**
     * Save a cancelTransaction.
     *
     * @param cancelTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    public CancelTransactionDTO save(CancelTransactionDTO cancelTransactionDTO) {
        log.debug("Request to save CancelTransaction : {}", cancelTransactionDTO);
        CancelTransaction cancelTransaction = cancelTransactionMapper.toEntity(cancelTransactionDTO);
        log.debug("get sale transaction");
        SaleTransactions sale = saleTransactionsRepository.findById(cancelTransaction.getSaleTransactions().getId()).get();
        sale.setStatusTransaction(StatusTransaction.CANCELED);

        log.debug("create product ids");
        List<Long> productIds = getProductIds(sale.getItems());
        log.debug("get sale products");
        List<Product> allById = productRepository.findAllById(productIds);
        log.debug("update product quantity");
        updateQty(allById, sale.getItems());

//        set cancel invoice number
        SequenceNumber currentInvoiceNo = sequenceNumberRepository.findByType("cancelInvoice");
        int currentValue = currentInvoiceNo.getNextValue();
        String noCanclelInvoice = generateInvoiceNo(currentInvoiceNo);
        currentInvoiceNo.setNextValue(++currentValue);
        sequenceNumberRepository.save(currentInvoiceNo);
        sequenceNumberSearchRepository.save(currentInvoiceNo);

        cancelTransaction.setNoCancelInvoice(noCanclelInvoice);

        log.debug("save all");
        cancelTransaction = cancelTransactionRepository.save(cancelTransaction);
        allById = productRepository.saveAll(allById);
        sale = saleTransactionsRepository.save(sale);


        cancelTransactionSearchRepository.save(cancelTransaction);
        productSearchRepository.saveAll(allById);
        saleTransactionsSearchRepository.save(sale);

        CancelTransactionDTO result = cancelTransactionMapper.toDto(cancelTransaction);

        return result;
    }

    private String generateInvoiceNo(SequenceNumber currentInvoice) {
        StringBuilder build = new StringBuilder();
        build.append(currentInvoice.getCodeType());
        build.append(String.format("%06d", currentInvoice.getNextValue()));
        log.debug("new Invoice No: " + build.toString());
        return build.toString();
    }

    private void updateQty(List<Product> products, Set<SaleItem> items) {
        products.forEach(product -> {
            items.forEach(item -> {
                if (item.getProduct().getId() == product.getId()) {
                    if (item.getQuantity() > product.getStock()) {
                        throw new RuntimeException("Product change");
                    }
                    product.setStock(product.getStock() + item.getQuantity());
                }
            });
        });
//        return products;
    }

    private List<Long> getProductIds(Set<SaleItem> items) {
        List<Long> ids = new ArrayList<>();
        for (SaleItem item : items) {
            ids.add(item.getProduct().getId());
        }

        return ids;
    }

    /**
     * Get all the cancelTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CancelTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CancelTransactions");
        return cancelTransactionRepository.findAll(pageable)
            .map(cancelTransactionMapper::toDto);
    }


    /**
     * Get one cancelTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CancelTransactionDTO> findOne(Long id) {
        log.debug("Request to get CancelTransaction : {}", id);
        return cancelTransactionRepository.findById(id)
            .map(cancelTransactionMapper::toDto);
    }

    /**
     * Delete the cancelTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CancelTransaction : {}", id);
        cancelTransactionRepository.deleteById(id);
        cancelTransactionSearchRepository.deleteById(id);
    }

    /**
     * Search for the cancelTransaction corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CancelTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CancelTransactions for query {}", query);
        return cancelTransactionSearchRepository.search(queryStringQuery(query), pageable)
            .map(cancelTransactionMapper::toDto);
    }
}
