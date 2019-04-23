package com.toko.maju.service.impl;

import com.toko.maju.service.SaleItemService;
import com.toko.maju.domain.SaleItem;
import com.toko.maju.repository.SaleItemRepository;
import com.toko.maju.repository.search.SaleItemSearchRepository;
import com.toko.maju.service.dto.SaleItemDTO;
import com.toko.maju.service.mapper.SaleItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SaleItem.
 */
@Service
@Transactional
public class SaleItemServiceImpl implements SaleItemService {

    private final Logger log = LoggerFactory.getLogger(SaleItemServiceImpl.class);

    private final SaleItemRepository saleItemRepository;

    private final SaleItemMapper saleItemMapper;

    private final SaleItemSearchRepository saleItemSearchRepository;

    public SaleItemServiceImpl(SaleItemRepository saleItemRepository, SaleItemMapper saleItemMapper, SaleItemSearchRepository saleItemSearchRepository) {
        this.saleItemRepository = saleItemRepository;
        this.saleItemMapper = saleItemMapper;
        this.saleItemSearchRepository = saleItemSearchRepository;
    }

    /**
     * Save a saleItem.
     *
     * @param saleItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SaleItemDTO save(SaleItemDTO saleItemDTO) {
        log.debug("Request to save SaleItem : {}", saleItemDTO);
        SaleItem saleItem = saleItemMapper.toEntity(saleItemDTO);
        saleItem = saleItemRepository.save(saleItem);
        SaleItemDTO result = saleItemMapper.toDto(saleItem);
        saleItemSearchRepository.save(saleItem);
        return result;
    }

    /**
     * Get all the saleItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaleItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SaleItems");
        return saleItemRepository.findAll(pageable)
            .map(saleItemMapper::toDto);
    }


    /**
     * Get one saleItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SaleItemDTO> findOne(Long id) {
        log.debug("Request to get SaleItem : {}", id);
        return saleItemRepository.findById(id)
            .map(saleItemMapper::toDto);
    }

    /**
     * Delete the saleItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SaleItem : {}", id);
        saleItemRepository.deleteById(id);
        saleItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the saleItem corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaleItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SaleItems for query {}", query);
        return saleItemSearchRepository.search(queryStringQuery(query), pageable)
            .map(saleItemMapper::toDto);
    }
}
