package com.toko.maju.service;

import com.toko.maju.service.dto.GeraiUpdateHistoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing GeraiUpdateHistory.
 */
public interface GeraiUpdateHistoryService {

    /**
     * Save a geraiUpdateHistory.
     *
     * @param geraiUpdateHistoryDTO the entity to save
     * @return the persisted entity
     */
    GeraiUpdateHistoryDTO save(GeraiUpdateHistoryDTO geraiUpdateHistoryDTO);

    /**
     * Get all the geraiUpdateHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeraiUpdateHistoryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" geraiUpdateHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeraiUpdateHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" geraiUpdateHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the geraiUpdateHistory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeraiUpdateHistoryDTO> search(String query, Pageable pageable);

    GeraiUpdateHistoryDTO findLatest();
}
