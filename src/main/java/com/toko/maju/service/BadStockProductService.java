package com.toko.maju.service;

import com.toko.maju.service.dto.BadStockProductDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing BadStockProduct.
 */
public interface BadStockProductService {

    /**
     * Save a badStockProduct.
     *
     * @param badStockProductDTO the entity to save
     * @return the persisted entity
     */
    BadStockProductDTO save(BadStockProductDTO badStockProductDTO);

    /**
     * Get all the badStockProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BadStockProductDTO> findAll(Pageable pageable);


    /**
     * Get the "id" badStockProduct.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BadStockProductDTO> findOne(Long id);

    /**
     * Delete the "id" badStockProduct.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the badStockProduct corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BadStockProductDTO> search(String query, Pageable pageable);
}
