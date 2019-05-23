package com.toko.maju.service;

import com.toko.maju.service.dto.GeraiConfigDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GeraiConfig.
 */
public interface GeraiConfigService {

    /**
     * Save a geraiConfig.
     *
     * @param geraiConfigDTO the entity to save
     * @return the persisted entity
     */
    GeraiConfigDTO save(GeraiConfigDTO geraiConfigDTO);

    /**
     * Get all the geraiConfigs.
     *
     * @return the list of entities
     */
    List<GeraiConfigDTO> findAll();


    /**
     * Get the "id" geraiConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeraiConfigDTO> findOne(Long id);

    /**
     * Delete the "id" geraiConfig.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the geraiConfig corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<GeraiConfigDTO> search(String query);
}
