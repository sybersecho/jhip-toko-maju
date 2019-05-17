package com.toko.maju.service.impl;

import com.toko.maju.service.BadStockProductService;
import com.toko.maju.domain.BadStockProduct;
import com.toko.maju.repository.BadStockProductRepository;
import com.toko.maju.repository.search.BadStockProductSearchRepository;
import com.toko.maju.service.dto.BadStockProductDTO;
import com.toko.maju.service.mapper.BadStockProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BadStockProduct.
 */
@Service
@Transactional
public class BadStockProductServiceImpl implements BadStockProductService {

    private final Logger log = LoggerFactory.getLogger(BadStockProductServiceImpl.class);

    private final BadStockProductRepository badStockProductRepository;

    private final BadStockProductMapper badStockProductMapper;

    private final BadStockProductSearchRepository badStockProductSearchRepository;

    public BadStockProductServiceImpl(BadStockProductRepository badStockProductRepository, BadStockProductMapper badStockProductMapper, BadStockProductSearchRepository badStockProductSearchRepository) {
        this.badStockProductRepository = badStockProductRepository;
        this.badStockProductMapper = badStockProductMapper;
        this.badStockProductSearchRepository = badStockProductSearchRepository;
    }

    /**
     * Save a badStockProduct.
     *
     * @param badStockProductDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BadStockProductDTO save(BadStockProductDTO badStockProductDTO) {
        log.debug("Request to save BadStockProduct : {}", badStockProductDTO);
        BadStockProduct badStockProduct = badStockProductMapper.toEntity(badStockProductDTO);
        badStockProduct = badStockProductRepository.save(badStockProduct);
        BadStockProductDTO result = badStockProductMapper.toDto(badStockProduct);
        badStockProductSearchRepository.save(badStockProduct);
        return result;
    }

    /**
     * Get all the badStockProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BadStockProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BadStockProducts");
        return badStockProductRepository.findAll(pageable)
            .map(badStockProductMapper::toDto);
    }


    /**
     * Get one badStockProduct by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BadStockProductDTO> findOne(Long id) {
        log.debug("Request to get BadStockProduct : {}", id);
        return badStockProductRepository.findById(id)
            .map(badStockProductMapper::toDto);
    }

    /**
     * Delete the badStockProduct by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BadStockProduct : {}", id);
        badStockProductRepository.deleteById(id);
        badStockProductSearchRepository.deleteById(id);
    }

    /**
     * Search for the badStockProduct corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BadStockProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BadStockProducts for query {}", query);
        return badStockProductSearchRepository.search(queryStringQuery(query), pageable)
            .map(badStockProductMapper::toDto);
    }
}
