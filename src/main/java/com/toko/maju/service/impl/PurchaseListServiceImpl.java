package com.toko.maju.service.impl;

import com.toko.maju.service.PurchaseListService;
import com.toko.maju.domain.PurchaseList;
import com.toko.maju.repository.PurchaseListRepository;
import com.toko.maju.repository.search.PurchaseListSearchRepository;
import com.toko.maju.service.dto.PurchaseListDTO;
import com.toko.maju.service.mapper.PurchaseListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PurchaseList.
 */
@Service
@Transactional
public class PurchaseListServiceImpl implements PurchaseListService {

    private final Logger log = LoggerFactory.getLogger(PurchaseListServiceImpl.class);

    private final PurchaseListRepository purchaseListRepository;

    private final PurchaseListMapper purchaseListMapper;

    private final PurchaseListSearchRepository purchaseListSearchRepository;

    public PurchaseListServiceImpl(PurchaseListRepository purchaseListRepository, PurchaseListMapper purchaseListMapper, PurchaseListSearchRepository purchaseListSearchRepository) {
        this.purchaseListRepository = purchaseListRepository;
        this.purchaseListMapper = purchaseListMapper;
        this.purchaseListSearchRepository = purchaseListSearchRepository;
    }

    /**
     * Save a purchaseList.
     *
     * @param purchaseListDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PurchaseListDTO save(PurchaseListDTO purchaseListDTO) {
        log.debug("Request to save PurchaseList : {}", purchaseListDTO);
        PurchaseList purchaseList = purchaseListMapper.toEntity(purchaseListDTO);
        purchaseList = purchaseListRepository.save(purchaseList);
        PurchaseListDTO result = purchaseListMapper.toDto(purchaseList);
        purchaseListSearchRepository.save(purchaseList);
        return result;
    }

    /**
     * Get all the purchaseLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseLists");
        return purchaseListRepository.findAll(pageable)
            .map(purchaseListMapper::toDto);
    }


    /**
     * Get one purchaseList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseListDTO> findOne(Long id) {
        log.debug("Request to get PurchaseList : {}", id);
        return purchaseListRepository.findById(id)
            .map(purchaseListMapper::toDto);
    }

    /**
     * Delete the purchaseList by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchaseList : {}", id);
        purchaseListRepository.deleteById(id);
        purchaseListSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchaseList corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseListDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchaseLists for query {}", query);
        return purchaseListSearchRepository.search(queryStringQuery(query), pageable)
            .map(purchaseListMapper::toDto);
    }
}
