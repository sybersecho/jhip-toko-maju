package com.toko.maju.service.impl;

import com.toko.maju.service.CustomerService;
import com.toko.maju.domain.Customer;
import com.toko.maju.repository.CustomerRepository;
import com.toko.maju.repository.search.CustomerSearchRepository;
import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private final CustomerRepository customerRepository;

	private final CustomerMapper customerMapper;

	private final CustomerSearchRepository customerSearchRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper,
			CustomerSearchRepository customerSearchRepository) {
		this.customerRepository = customerRepository;
		this.customerMapper = customerMapper;
		this.customerSearchRepository = customerSearchRepository;
	}

	/**
	 * Save a customer.
	 *
	 * @param customerDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public CustomerDTO save(CustomerDTO customerDTO) {
		log.debug("Request to save Customer : {}", customerDTO);
		Customer customer = customerMapper.toEntity(customerDTO);
		customer = customerRepository.save(customer);
		CustomerDTO result = customerMapper.toDto(customer);
		customerSearchRepository.save(customer);
		return result;
	}

	/**
	 * Get all the customers.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CustomerDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Customers");
		return customerRepository.findAll(pageable).map(customerMapper::toDto);
	}

	/**
	 * Get one customer by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> findOne(Long id) {
		log.debug("Request to get Customer : {}", id);
		return customerRepository.findById(id).map(customerMapper::toDto);
	}

	/**
	 * Delete the customer by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Customer : {}", id);
		customerRepository.deleteById(id);
		customerSearchRepository.deleteById(id);
	}

	/**
	 * Search for the customer corresponding to the query.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CustomerDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of Customers for query {}", query);
		return customerSearchRepository.search(queryStringQuery(query), pageable).map(customerMapper::toDto);
	}
}
