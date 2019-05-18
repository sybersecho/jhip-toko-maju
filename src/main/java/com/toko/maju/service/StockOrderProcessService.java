package com.toko.maju.service;

import com.toko.maju.service.dto.StockOrderProcessDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing StockOrderProcess.
 */
public interface StockOrderProcessService {

    /**
     * Save a stockOrderProcess.
     *
     * @param stockOrderProcessDTO the entity to save
     * @return the persisted entity
     */
    StockOrderProcessDTO save(StockOrderProcessDTO stockOrderProcessDTO);

    /**
     * Get all the stockOrderProcesses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderProcessDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stockOrderProcess.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockOrderProcessDTO> findOne(Long id);

    /**
     * Delete the "id" stockOrderProcess.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockOrderProcess corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockOrderProcessDTO> search(String query, Pageable pageable);

    List<StockOrderProcessDTO> saveAll(List<StockOrderProcessDTO> stockOrderProcessDTOS);
}
