package com.toko.maju.service;

import com.toko.maju.service.dto.StockOrderReceiveDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing StockOrderReceive.
 */
public interface StockOrderReceiveService {

    /**
     * Save a stockOrderReceive.
     *
     * @param stockOrderReceiveDTO the entity to save
     * @return the persisted entity
     */
    StockOrderReceiveDTO save(StockOrderReceiveDTO stockOrderReceiveDTO);

    /**
     * Get all the stockOrderReceives.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderReceiveDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stockOrderReceive.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockOrderReceiveDTO> findOne(Long id);

    /**
     * Delete the "id" stockOrderReceive.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockOrderReceive corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderReceiveDTO> search(String query, Pageable pageable);

    List<StockOrderReceiveDTO> saveAll(List<StockOrderReceiveDTO> stockOrderReceiveDTOS);
}
