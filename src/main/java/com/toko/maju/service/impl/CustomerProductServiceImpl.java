package com.toko.maju.service.impl;

import com.toko.maju.service.CustomerProductService;
import com.toko.maju.domain.CustomerProduct;
import com.toko.maju.repository.CustomerProductRepository;
import com.toko.maju.repository.search.CustomerProductSearchRepository;
import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.dto.CustomerProductDTO;
import com.toko.maju.service.mapper.CustomerMapper;
import com.toko.maju.service.mapper.CustomerProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomerProduct.
 */
@Service
@Transactional
public class CustomerProductServiceImpl implements CustomerProductService {

    private final Logger log = LoggerFactory.getLogger(CustomerProductServiceImpl.class);

    private final CustomerProductRepository customerProductRepository;

    private final CustomerProductMapper customerProductMapper;

    private final CustomerProductSearchRepository customerProductSearchRepository;
    
    @Autowired
    private final CustomerMapper customerMapper = null;

    public CustomerProductServiceImpl(CustomerProductRepository customerProductRepository, CustomerProductMapper customerProductMapper, CustomerProductSearchRepository customerProductSearchRepository) {
        this.customerProductRepository = customerProductRepository;
        this.customerProductMapper = customerProductMapper;
        this.customerProductSearchRepository = customerProductSearchRepository;
    }

    /**
     * Save a customerProduct.
     *
     * @param customerProductDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerProductDTO save(CustomerProductDTO customerProductDTO) {
        log.debug("Request to save CustomerProduct : {}", customerProductDTO);
        CustomerProduct customerProduct = customerProductMapper.toEntity(customerProductDTO);
        customerProduct = customerProductRepository.save(customerProduct);
        CustomerProductDTO result = customerProductMapper.toDto(customerProduct);
        customerProductSearchRepository.save(customerProduct);
        return result;
    }

    /**
     * Get all the customerProducts.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerProductDTO> findAll() {
        log.debug("Request to get all CustomerProducts");
        return customerProductRepository.findAll().stream()
            .map(customerProductMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one customerProduct by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerProductDTO> findOne(Long id) {
        log.debug("Request to get CustomerProduct : {}", id);
        return customerProductRepository.findById(id)
            .map(customerProductMapper::toDto);
    }

    /**
     * Delete the customerProduct by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerProduct : {}", id);        customerProductRepository.deleteById(id);
        customerProductSearchRepository.deleteById(id);
    }

    /**
     * Search for the customerProduct corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerProductDTO> search(String query) {
        log.debug("Request to search CustomerProducts for query {}", query);
        return StreamSupport
            .stream(customerProductSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(customerProductMapper::toDto)
            .collect(Collectors.toList());
    }
    
    @Override
	public CustomerProductDTO saveCustomerProducts(List<CustomerProductDTO> productDTOs) {
    	log.debug("save customer products");
    	customerProductRepository.saveAll(customerProductMapper.toEntity(productDTOs));    	
    	return productDTOs.get(0);
    }
    
    /**
     * Get all the customerProducts by Customer.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerProductDTO> findByCustomer(CustomerDTO customerDTO) {
        log.debug("Request to get all CustomerProducts by {}", customerDTO);
        return customerProductRepository.findByCustomer(customerMapper.toEntity(customerDTO))
        	.stream()
            .map(customerProductMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
