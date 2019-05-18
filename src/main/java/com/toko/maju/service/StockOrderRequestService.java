package com.toko.maju.service;

import com.toko.maju.service.dto.StockOrderRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StockOrderRequest.
 */
public interface StockOrderRequestService {

    /**
     * Save a stockOrderRequest.
     *
     * @param stockOrderRequestDTO the entity to save
     * @return the persisted entity
     */
    StockOrderRequestDTO save(StockOrderRequestDTO stockOrderRequestDTO);

    /**
     * Get all the stockOrderRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderRequestDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stockOrderRequest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockOrderRequestDTO> findOne(Long id);

    /**
     * Delete the "id" stockOrderRequest.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockOrderRequest corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderRequestDTO> search(String query, Pageable pageable);
}
