package com.toko.maju.service;

import com.toko.maju.service.dto.ReturnTransactionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ReturnTransaction.
 */
public interface ReturnTransactionService {

    /**
     * Save a returnTransaction.
     *
     * @param returnTransactionDTO the entity to save
     * @return the persisted entity
     */
    ReturnTransactionDTO save(ReturnTransactionDTO returnTransactionDTO);

    /**
     * Get all the returnTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReturnTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" returnTransaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ReturnTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" returnTransaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the returnTransaction corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReturnTransactionDTO> search(String query, Pageable pageable);
}
