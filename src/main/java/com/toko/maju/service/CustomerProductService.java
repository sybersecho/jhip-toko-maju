package com.toko.maju.service;

import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.dto.CustomerProductDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing CustomerProduct.
 */
public interface CustomerProductService {

    /**
     * Save a customerProduct.
     *
     * @param customerProductDTO the entity to save
     * @return the persisted entity
     */
    CustomerProductDTO save(CustomerProductDTO customerProductDTO);

    /**
     * Get all the customerProducts.
     *
     * @return the list of entities
     */
    List<CustomerProductDTO> findAll();


    /**
     * Get the "id" customerProduct.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CustomerProductDTO> findOne(Long id);

    /**
     * Delete the "id" customerProduct.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customerProduct corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CustomerProductDTO> search(String query);
    
    /**
     * Save Customer Product for a Customer.
     *
     * @param productDTOs the entity to save
     * 
     * @return the persisted entity
     */
	CustomerProductDTO saveCustomerProducts(List<CustomerProductDTO> productDTOs);

	/**
     * Search for the customerProduct corresponding to the query.
     *
     * @param customerDTO query the query of the search
     * 
     * @return the list of entities
     */
	List<CustomerProductDTO> findByCustomer(CustomerDTO customerDTO);
}
