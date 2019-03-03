package com.toko.maju.service.impl;

import com.toko.maju.service.CustomerProductService;
import com.toko.maju.domain.CustomerProduct;
import com.toko.maju.repository.CustomerProductRepository;
import com.toko.maju.repository.search.CustomerProductSearchRepository;
import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.dto.CustomerProductDTO;
import com.toko.maju.service.mapper.CustomerProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerProducts");
        return customerProductRepository.findAll(pageable)
            .map(customerProductMapper::toDto);
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
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerProducts for query {}", query);
        return customerProductSearchRepository.search(queryStringQuery(query), pageable)
            .map(customerProductMapper::toDto);
    }

	@Override
	public CustomerProductDTO batchSaveCustomer(List<CustomerProductDTO> customerProductDTOs) {
		log.debug("Request to save CustomerProducts :");
		customerProductRepository.saveAll(customerProductMapper.toEntity(customerProductDTOs));
		return customerProductDTOs.get(0);
	}
}
