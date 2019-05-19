package com.toko.maju.service;

import com.toko.maju.service.dto.PurchaseListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing PurchaseList.
 */
public interface PurchaseListService {

    /**
     * Save a purchaseList.
     *
     * @param purchaseListDTO the entity to save
     * @return the persisted entity
     */
    PurchaseListDTO save(PurchaseListDTO purchaseListDTO);

    /**
     * Get all the purchaseLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PurchaseListDTO> findAll(Pageable pageable);


    /**
     * Get the "id" purchaseList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PurchaseListDTO> findOne(Long id);

    /**
     * Delete the "id" purchaseList.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the purchaseList corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PurchaseListDTO> search(String query, Pageable pageable);
}
