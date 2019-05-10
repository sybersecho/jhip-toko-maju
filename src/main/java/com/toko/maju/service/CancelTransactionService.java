package com.toko.maju.service;

import com.toko.maju.service.dto.CancelTransactionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CancelTransaction.
 */
public interface CancelTransactionService {

    /**
     * Save a cancelTransaction.
     *
     * @param cancelTransactionDTO the entity to save
     * @return the persisted entity
     */
    CancelTransactionDTO save(CancelTransactionDTO cancelTransactionDTO);

    /**
     * Get all the cancelTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CancelTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" cancelTransaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CancelTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" cancelTransaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cancelTransaction corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CancelTransactionDTO> search(String query, Pageable pageable);
}
