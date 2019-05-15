package com.toko.maju.service.impl;

import com.toko.maju.domain.Product;
import com.toko.maju.domain.ReturnItem;
import com.toko.maju.domain.SequenceNumber;
import com.toko.maju.domain.enumeration.ProductStatus;
import com.toko.maju.domain.enumeration.TransactionType;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.SequenceNumberRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.repository.search.SequenceNumberSearchRepository;
import com.toko.maju.service.ReturnTransactionService;
import com.toko.maju.domain.ReturnTransaction;
import com.toko.maju.repository.ReturnTransactionRepository;
import com.toko.maju.repository.search.ReturnTransactionSearchRepository;
import com.toko.maju.service.dto.ReturnTransactionDTO;
import com.toko.maju.service.mapper.ReturnTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReturnTransaction.
 */
@Service
@Transactional
public class ReturnTransactionServiceImpl implements ReturnTransactionService {

    private final Logger log = LoggerFactory.getLogger(ReturnTransactionServiceImpl.class);

    private final ReturnTransactionRepository returnTransactionRepository;

    private final ReturnTransactionMapper returnTransactionMapper;

    private final ReturnTransactionSearchRepository returnTransactionSearchRepository;

    @Autowired
    private final ProductRepository productRepository = null;

    @Autowired
    private final ProductSearchRepository productSearchRepository = null;

    @Autowired
    private final SequenceNumberRepository sequenceNumberRepository = null;

    @Autowired
    private final SequenceNumberSearchRepository sequenceNumberSearchRepository = null;

    public ReturnTransactionServiceImpl(ReturnTransactionRepository returnTransactionRepository, ReturnTransactionMapper returnTransactionMapper, ReturnTransactionSearchRepository returnTransactionSearchRepository) {
        this.returnTransactionRepository = returnTransactionRepository;
        this.returnTransactionMapper = returnTransactionMapper;
        this.returnTransactionSearchRepository = returnTransactionSearchRepository;
    }

    /**
     * Save a returnTransaction.
     *
     * @param returnTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    public ReturnTransactionDTO save(ReturnTransactionDTO returnTransactionDTO) {
        log.debug("Request to save ReturnTransaction : {}", returnTransactionDTO);
        ReturnTransaction returnTransaction = returnTransactionMapper.toEntity(returnTransactionDTO);

//        set no transaction
        SequenceNumber currentNoTransaction = (returnTransaction.getTransactionType() == TransactionType.SHOP) ?
            sequenceNumberRepository.findByType("returnToko") : sequenceNumberRepository.findByType("returnSupllier");
        int currentValue = currentNoTransaction.getNextValue();
        String noTransaction = generateNoTransaction(currentNoTransaction);
        currentNoTransaction.setNextValue(++currentValue);
        sequenceNumberRepository.save(currentNoTransaction);

        returnTransaction.setNoTransaction(noTransaction);
        returnTransaction = returnTransactionRepository.save(returnTransaction);
        // get ReturnItem
        Set<ReturnItem> items = returnTransaction.getReturnItems();
        //create bad and good product
        Map<Long, Integer> goodProducts = new HashMap<>();
        Map<Long, Integer> badProducts = new HashMap<>();
        filterItems(items, goodProducts, badProducts);

        if (!goodProducts.isEmpty()) {
            // load products by good ids
            Set<Product> products = productRepository.findAllById(goodProducts.keySet()).stream().collect(Collectors.toCollection(HashSet::new));

            // update good product qty
            log.debug("Items: {}", products);
            log.debug("good product: {}", goodProducts.keySet());
            products = updateQty(goodProducts, products);
            log.debug("Updated Product: {}", products);


            // update product
            productRepository.saveAll(products);
            productSearchRepository.saveAll(products);
        }

        if (!badProducts.isEmpty()) {
            // TODO:: GET AND UPDATE BAD PRODUCT
        }

        ReturnTransactionDTO result = returnTransactionMapper.toDto(returnTransaction);
        returnTransactionSearchRepository.save(returnTransaction);
        return result;
    }

    private String generateNoTransaction(SequenceNumber currentNoTransaction) {
        StringBuilder build = new StringBuilder();
        build.append(currentNoTransaction.getCodeType());
        build.append(String.format("%06d", currentNoTransaction.getNextValue()));
        log.debug("new No Transaction: " + build.toString());
        return build.toString();
    }

    private void filterItems(Set<ReturnItem> items, Map<Long, Integer> goodProducts, Map<Long, Integer> badProducts) {
        items.forEach(item -> {
            if (ProductStatus.GOOD == item.getProductStatus()) {
                goodProducts.put(item.getProduct().getId(), item.getQuantity());
            } else
                badProducts.put(item.getProduct().getId(), item.getQuantity());
        });
    }

    private Set<Product> updateQty(Map<Long, Integer> items, Set<Product> products) {
        products.forEach(product -> {
            log.debug("product before: {}", product);
            product.setStock(product.getStock() + items.get(product.getId()));
            log.debug("product after: {}", product);
        });
        return products;
    }

    /**
     * Get all the returnTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReturnTransactions");
        return returnTransactionRepository.findAll(pageable)
            .map(returnTransactionMapper::toDto);
    }


    /**
     * Get one returnTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReturnTransactionDTO> findOne(Long id) {
        log.debug("Request to get ReturnTransaction : {}", id);
        return returnTransactionRepository.findById(id)
            .map(returnTransactionMapper::toDto);
    }

    /**
     * Delete the returnTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReturnTransaction : {}", id);
        returnTransactionRepository.deleteById(id);
        returnTransactionSearchRepository.deleteById(id);
    }

    /**
     * Search for the returnTransaction corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReturnTransactions for query {}", query);
        return returnTransactionSearchRepository.search(queryStringQuery(query), pageable)
            .map(returnTransactionMapper::toDto);
    }
}
