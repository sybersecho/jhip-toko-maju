package com.toko.maju.service;

import com.toko.maju.service.dto.SaleItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SaleItem.
 */
public interface SaleItemService {

    /**
     * Save a saleItem.
     *
     * @param saleItemDTO the entity to save
     * @return the persisted entity
     */
    SaleItemDTO save(SaleItemDTO saleItemDTO);

    /**
     * Get all the saleItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" saleItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SaleItemDTO> findOne(Long id);

    /**
     * Delete the "id" saleItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the saleItem corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleItemDTO> search(String query, Pageable pageable);
}
