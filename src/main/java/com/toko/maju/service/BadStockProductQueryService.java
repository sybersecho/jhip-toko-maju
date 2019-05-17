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

import com.toko.maju.domain.BadStockProduct;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.BadStockProductRepository;
import com.toko.maju.repository.search.BadStockProductSearchRepository;
import com.toko.maju.service.dto.BadStockProductCriteria;
import com.toko.maju.service.dto.BadStockProductDTO;
import com.toko.maju.service.mapper.BadStockProductMapper;

/**
 * Service for executing complex queries for BadStockProduct entities in the database.
 * The main input is a {@link BadStockProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BadStockProductDTO} or a {@link Page} of {@link BadStockProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BadStockProductQueryService extends QueryService<BadStockProduct> {

    private final Logger log = LoggerFactory.getLogger(BadStockProductQueryService.class);

    private final BadStockProductRepository badStockProductRepository;

    private final BadStockProductMapper badStockProductMapper;

    private final BadStockProductSearchRepository badStockProductSearchRepository;

    public BadStockProductQueryService(BadStockProductRepository badStockProductRepository, BadStockProductMapper badStockProductMapper, BadStockProductSearchRepository badStockProductSearchRepository) {
        this.badStockProductRepository = badStockProductRepository;
        this.badStockProductMapper = badStockProductMapper;
        this.badStockProductSearchRepository = badStockProductSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BadStockProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BadStockProductDTO> findByCriteria(BadStockProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BadStockProduct> specification = createSpecification(criteria);
        return badStockProductMapper.toDto(badStockProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BadStockProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BadStockProductDTO> findByCriteria(BadStockProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BadStockProduct> specification = createSpecification(criteria);
        return badStockProductRepository.findAll(specification, page)
            .map(badStockProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BadStockProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BadStockProduct> specification = createSpecification(criteria);
        return badStockProductRepository.count(specification);
    }

    /**
     * Function to convert BadStockProductCriteria to a {@link Specification}
     */
    private Specification<BadStockProduct> createSpecification(BadStockProductCriteria criteria) {
        Specification<BadStockProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BadStockProduct_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), BadStockProduct_.barcode));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), BadStockProduct_.productName));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), BadStockProduct_.quantity));
            }
        }
        return specification;
    }
}
