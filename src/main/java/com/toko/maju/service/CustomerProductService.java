package com.toko.maju.service;

import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.dto.CustomerProductDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<CustomerProductDTO> findAll(Pageable pageable);

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
	 * @param query    the query of the search
	 * 
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<CustomerProductDTO> search(String query, Pageable pageable);

	CustomerProductDTO batchSaveCustomer(List<CustomerProductDTO> customerProductDTOs);
}
