package com.toko.maju.service;

import com.toko.maju.service.dto.GeraiDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Gerai.
 */
public interface GeraiService {

    /**
     * Save a gerai.
     *
     * @param geraiDTO the entity to save
     * @return the persisted entity
     */
    GeraiDTO save(GeraiDTO geraiDTO);

    /**
     * Get all the gerais.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeraiDTO> findAll(Pageable pageable);


    /**
     * Get the "id" gerai.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeraiDTO> findOne(Long id);

    /**
     * Delete the "id" gerai.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the gerai corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeraiDTO> search(String query, Pageable pageable);
}
