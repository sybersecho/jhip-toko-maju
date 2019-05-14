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

import com.toko.maju.domain.ReturnItem;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.ReturnItemRepository;
import com.toko.maju.repository.search.ReturnItemSearchRepository;
import com.toko.maju.service.dto.ReturnItemCriteria;
import com.toko.maju.service.dto.ReturnItemDTO;
import com.toko.maju.service.mapper.ReturnItemMapper;

/**
 * Service for executing complex queries for ReturnItem entities in the database.
 * The main input is a {@link ReturnItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReturnItemDTO} or a {@link Page} of {@link ReturnItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReturnItemQueryService extends QueryService<ReturnItem> {

    private final Logger log = LoggerFactory.getLogger(ReturnItemQueryService.class);

    private final ReturnItemRepository returnItemRepository;

    private final ReturnItemMapper returnItemMapper;

    private final ReturnItemSearchRepository returnItemSearchRepository;

    public ReturnItemQueryService(ReturnItemRepository returnItemRepository, ReturnItemMapper returnItemMapper, ReturnItemSearchRepository returnItemSearchRepository) {
        this.returnItemRepository = returnItemRepository;
        this.returnItemMapper = returnItemMapper;
        this.returnItemSearchRepository = returnItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReturnItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReturnItemDTO> findByCriteria(ReturnItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReturnItem> specification = createSpecification(criteria);
        return returnItemMapper.toDto(returnItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReturnItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReturnItemDTO> findByCriteria(ReturnItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReturnItem> specification = createSpecification(criteria);
        return returnItemRepository.findAll(specification, page)
            .map(returnItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReturnItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReturnItem> specification = createSpecification(criteria);
        return returnItemRepository.count(specification);
    }

    /**
     * Function to convert ReturnItemCriteria to a {@link Specification}
     */
    private Specification<ReturnItem> createSpecification(ReturnItemCriteria criteria) {
        Specification<ReturnItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ReturnItem_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), ReturnItem_.barcode));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), ReturnItem_.productName));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ReturnItem_.quantity));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), ReturnItem_.unitPrice));
            }
            if (criteria.getProductStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getProductStatus(), ReturnItem_.productStatus));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), ReturnItem_.unit));
            }
            if (criteria.getTotalItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalItemPrice(), ReturnItem_.totalItemPrice));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(ReturnItem_.product, JoinType.LEFT).get(Product_.id)));
            }
            if (criteria.getReturnTransactionId() != null) {
                specification = specification.and(buildSpecification(criteria.getReturnTransactionId(),
                    root -> root.join(ReturnItem_.returnTransaction, JoinType.LEFT).get(ReturnTransaction_.id)));
            }
        }
        return specification;
    }
}
