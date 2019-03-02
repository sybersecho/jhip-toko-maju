package com.toko.maju.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.toko.maju.domain.CustomerProduct;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.CustomerProductRepository;
import com.toko.maju.repository.search.CustomerProductSearchRepository;
import com.toko.maju.service.dto.CustomerProductCriteria;
import com.toko.maju.service.dto.CustomerProductDTO;
import com.toko.maju.service.mapper.CustomerProductMapper;

/**
 * Service for executing complex queries for CustomerProduct entities in the database.
 * The main input is a {@link CustomerProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerProductDTO} or a {@link Page} of {@link CustomerProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerProductQueryService extends QueryService<CustomerProduct> {

    private final Logger log = LoggerFactory.getLogger(CustomerProductQueryService.class);

    private final CustomerProductRepository customerProductRepository;

    private final CustomerProductMapper customerProductMapper;

    private final CustomerProductSearchRepository customerProductSearchRepository;

    public CustomerProductQueryService(CustomerProductRepository customerProductRepository, CustomerProductMapper customerProductMapper, CustomerProductSearchRepository customerProductSearchRepository) {
        this.customerProductRepository = customerProductRepository;
        this.customerProductMapper = customerProductMapper;
        this.customerProductSearchRepository = customerProductSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerProductDTO> findByCriteria(CustomerProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerProduct> specification = createSpecification(criteria);
        return customerProductMapper.toDto(customerProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerProductDTO> findByCriteria(CustomerProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerProduct> specification = createSpecification(criteria);
        return customerProductRepository.findAll(specification, page)
            .map(customerProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerProduct> specification = createSpecification(criteria);
        return customerProductRepository.count(specification);
    }

    /**
     * Function to convert CustomerProductCriteria to a {@link Specification}
     */
    private Specification<CustomerProduct> createSpecification(CustomerProductCriteria criteria) {
        Specification<CustomerProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CustomerProduct_.id));
            }
            if (criteria.getSpecialPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpecialPrice(), CustomerProduct_.specialPrice));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(CustomerProduct_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}
