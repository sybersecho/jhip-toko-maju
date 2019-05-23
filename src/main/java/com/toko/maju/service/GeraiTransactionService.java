package com.toko.maju.service;

import com.toko.maju.service.dto.GeraiTransactionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GeraiTransaction.
 */
public interface GeraiTransactionService {

    /**
     * Save a geraiTransaction.
     *
     * @param geraiTransactionDTO the entity to save
     * @return the persisted entity
     */
    GeraiTransactionDTO save(GeraiTransactionDTO geraiTransactionDTO);

    /**
     * Get all the geraiTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeraiTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" geraiTransaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeraiTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" geraiTransaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the geraiTransaction corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeraiTransactionDTO> search(String query, Pageable pageable);

    List<GeraiTransactionDTO> saveAll(List<GeraiTransactionDTO> geraiTransactionDTOS);
}
