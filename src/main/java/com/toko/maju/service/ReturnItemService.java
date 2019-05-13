package com.toko.maju.service;

import com.toko.maju.service.dto.ReturnItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ReturnItem.
 */
public interface ReturnItemService {

    /**
     * Save a returnItem.
     *
     * @param returnItemDTO the entity to save
     * @return the persisted entity
     */
    ReturnItemDTO save(ReturnItemDTO returnItemDTO);

    /**
     * Get all the returnItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReturnItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" returnItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ReturnItemDTO> findOne(Long id);

    /**
     * Delete the "id" returnItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the returnItem corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReturnItemDTO> search(String query, Pageable pageable);
}
