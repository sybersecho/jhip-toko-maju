package com.toko.maju.service;

import com.toko.maju.service.dto.SaleTransactionsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SaleTransactions.
 */
public interface SaleTransactionsService {

    /**
     * Save a saleTransactions.
     *
     * @param saleTransactionsDTO the entity to save
     * @return the persisted entity
     */
    SaleTransactionsDTO save(SaleTransactionsDTO saleTransactionsDTO);

    /**
     * Get all the saleTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleTransactionsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" saleTransactions.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SaleTransactionsDTO> findOne(Long id);

    /**
     * Delete the "id" saleTransactions.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the saleTransactions corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleTransactionsDTO> search(String query, Pageable pageable);
}
