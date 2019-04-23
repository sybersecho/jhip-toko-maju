package com.toko.maju.service;

import com.toko.maju.service.dto.DuePaymentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DuePayment.
 */
public interface DuePaymentService {

    /**
     * Save a duePayment.
     *
     * @param duePaymentDTO the entity to save
     * @return the persisted entity
     */
    DuePaymentDTO save(DuePaymentDTO duePaymentDTO);

    /**
     * Get all the duePayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DuePaymentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" duePayment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DuePaymentDTO> findOne(Long id);

    /**
     * Delete the "id" duePayment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the duePayment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DuePaymentDTO> search(String query, Pageable pageable);
}
