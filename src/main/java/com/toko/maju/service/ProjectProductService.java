package com.toko.maju.service;

import com.toko.maju.service.dto.ProjectProductDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ProjectProduct.
 */
public interface ProjectProductService {

    /**
     * Save a projectProduct.
     *
     * @param projectProductDTO the entity to save
     * @return the persisted entity
     */
    ProjectProductDTO save(ProjectProductDTO projectProductDTO);

    /**
     * Get all the projectProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProjectProductDTO> findAll(Pageable pageable);


    /**
     * Get the "id" projectProduct.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProjectProductDTO> findOne(Long id);

    /**
     * Delete the "id" projectProduct.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the projectProduct corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProjectProductDTO> search(String query, Pageable pageable);
}
