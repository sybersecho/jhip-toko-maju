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

import com.toko.maju.domain.SaleItem;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.SaleItemRepository;
import com.toko.maju.repository.search.SaleItemSearchRepository;
import com.toko.maju.service.dto.SaleItemCriteria;
import com.toko.maju.service.dto.SaleItemDTO;
import com.toko.maju.service.mapper.SaleItemMapper;

/**
 * Service for executing complex queries for SaleItem entities in the database.
 * The main input is a {@link SaleItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SaleItemDTO} or a {@link Page} of {@link SaleItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaleItemQueryService extends QueryService<SaleItem> {

    private final Logger log = LoggerFactory.getLogger(SaleItemQueryService.class);

    private final SaleItemRepository saleItemRepository;

    private final SaleItemMapper saleItemMapper;

    private final SaleItemSearchRepository saleItemSearchRepository;

    public SaleItemQueryService(SaleItemRepository saleItemRepository, SaleItemMapper saleItemMapper, SaleItemSearchRepository saleItemSearchRepository) {
        this.saleItemRepository = saleItemRepository;
        this.saleItemMapper = saleItemMapper;
        this.saleItemSearchRepository = saleItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SaleItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SaleItemDTO> findByCriteria(SaleItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SaleItem> specification = createSpecification(criteria);
        return saleItemMapper.toDto(saleItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SaleItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaleItemDTO> findByCriteria(SaleItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SaleItem> specification = createSpecification(criteria);
        return saleItemRepository.findAll(specification, page)
            .map(saleItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SaleItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SaleItem> specification = createSpecification(criteria);
        return saleItemRepository.count(specification);
    }

    /**
     * Function to convert SaleItemCriteria to a {@link Specification}
     */
    private Specification<SaleItem> createSpecification(SaleItemCriteria criteria) {
        Specification<SaleItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SaleItem_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), SaleItem_.quantity));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), SaleItem_.totalPrice));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(SaleItem_.product, JoinType.LEFT).get(Product_.id)));
            }
            if (criteria.getSaleId() != null) {
                specification = specification.and(buildSpecification(criteria.getSaleId(),
                    root -> root.join(SaleItem_.sale, JoinType.LEFT).get(SaleTransactions_.id)));
            }
        }
        return specification;
    }
}
