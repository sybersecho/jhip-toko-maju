package com.toko.maju.service.impl;

import com.toko.maju.domain.Product;
import com.toko.maju.domain.PurchaseList;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.service.PurchaseService;
import com.toko.maju.domain.Purchase;
import com.toko.maju.repository.PurchaseRepository;
import com.toko.maju.repository.search.PurchaseSearchRepository;
import com.toko.maju.service.dto.PurchaseDTO;
import com.toko.maju.service.mapper.PurchaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Purchase.
 */
@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    private final PurchaseRepository purchaseRepository;

    private final PurchaseMapper purchaseMapper;

    private final PurchaseSearchRepository purchaseSearchRepository;

    @Autowired
    private final ProductRepository productRepository = null;

    @Autowired
    private final ProductSearchRepository productSearchRepository = null;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper, PurchaseSearchRepository purchaseSearchRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.purchaseSearchRepository = purchaseSearchRepository;
    }

    /**
     * Save a purchase.
     *
     * @param purchaseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PurchaseDTO save(PurchaseDTO purchaseDTO) {
        log.debug("Request to save Purchase : {}", purchaseDTO);
        Purchase purchase = purchaseMapper.toEntity(purchaseDTO);
        Set<PurchaseList> purchaseLists = purchase.getPurchaseLists();

        HashMap<String, PurchaseList> productMap = new HashMap<>();
        createProductMap(productMap, purchaseLists);
        log.debug("search products with barcodes are {}", productMap.keySet());
        List<Product> products = productRepository.findByBarcodeIn(productMap.keySet());
        if (!products.isEmpty()) {
            log.debug("found {} product", products.size());
            log.debug("Product before {}", products);
            products.forEach(product -> {
                product.setStock(product.getStock() + productMap.get(product.getBarcode()).getQuantity());
                product.setUnitPrice(productMap.get(product.getBarcode()).getUnitPrice());
            });
            log.debug("product after {}", products);

            products = productRepository.saveAll(products);
            productSearchRepository.saveAll(products);
        }

        purchase = purchaseRepository.save(purchase);
        PurchaseDTO result = purchaseMapper.toDto(purchase);
        purchaseSearchRepository.save(purchase);
        return result;
    }

    private void createProductMap(HashMap<String, PurchaseList> productMap, Set<PurchaseList> purchaseLists) {
        purchaseLists.forEach(it -> productMap.put(it.getBarcode(), it));
    }

    /**
     * Get all the purchases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Purchases");
        return purchaseRepository.findAll(pageable)
            .map(purchaseMapper::toDto);
    }


    /**
     * Get one purchase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseDTO> findOne(Long id) {
        log.debug("Request to get Purchase : {}", id);
        return purchaseRepository.findById(id)
            .map(purchaseMapper::toDto);
    }

    /**
     * Delete the purchase by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Purchase : {}", id);
        purchaseRepository.deleteById(id);
        purchaseSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchase corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Purchases for query {}", query);
        return purchaseSearchRepository.search(queryStringQuery(query), pageable)
            .map(purchaseMapper::toDto);
    }
}
