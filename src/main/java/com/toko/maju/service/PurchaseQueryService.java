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

import com.toko.maju.domain.Purchase;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.PurchaseRepository;
import com.toko.maju.repository.search.PurchaseSearchRepository;
import com.toko.maju.service.dto.PurchaseCriteria;
import com.toko.maju.service.dto.PurchaseDTO;
import com.toko.maju.service.mapper.PurchaseMapper;

/**
 * Service for executing complex queries for Purchase entities in the database.
 * The main input is a {@link PurchaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseDTO} or a {@link Page} of {@link PurchaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseQueryService extends QueryService<Purchase> {

    private final Logger log = LoggerFactory.getLogger(PurchaseQueryService.class);

    private final PurchaseRepository purchaseRepository;

    private final PurchaseMapper purchaseMapper;

    private final PurchaseSearchRepository purchaseSearchRepository;

    public PurchaseQueryService(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper, PurchaseSearchRepository purchaseSearchRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.purchaseSearchRepository = purchaseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseDTO> findByCriteria(PurchaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Purchase> specification = createSpecification(criteria);
        return purchaseMapper.toDto(purchaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PurchaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseDTO> findByCriteria(PurchaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Purchase> specification = createSpecification(criteria);
        return purchaseRepository.findAll(specification, page)
            .map(purchaseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Purchase> specification = createSpecification(criteria);
        return purchaseRepository.count(specification);
    }

    /**
     * Function to convert PurchaseCriteria to a {@link Specification}
     */
    private Specification<Purchase> createSpecification(PurchaseCriteria criteria) {
        Specification<Purchase> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Purchase_.id));
            }
            if (criteria.getTotalPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPayment(), Purchase_.totalPayment));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Purchase_.createdDate));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Purchase_.note));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(Purchase_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(Purchase_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getPurchaseListId() != null) {
                specification = specification.and(buildSpecification(criteria.getPurchaseListId(),
                    root -> root.join(Purchase_.purchaseLists, JoinType.LEFT).get(PurchaseList_.id)));
            }
        }
        return specification;
    }
}
