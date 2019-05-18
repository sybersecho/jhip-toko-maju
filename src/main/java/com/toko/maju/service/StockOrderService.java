package com.toko.maju.service;

import com.toko.maju.service.dto.StockOrderDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StockOrder.
 */
public interface StockOrderService {

    /**
     * Save a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    StockOrderDTO save(StockOrderDTO stockOrderDTO);

    /**
     * Get all the stockOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stockOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockOrderDTO> findOne(Long id);

    /**
     * Delete the "id" stockOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockOrder corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderDTO> search(String query, Pageable pageable);
}
