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

import com.toko.maju.domain.PurchaseList;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.PurchaseListRepository;
import com.toko.maju.repository.search.PurchaseListSearchRepository;
import com.toko.maju.service.dto.PurchaseListCriteria;
import com.toko.maju.service.dto.PurchaseListDTO;
import com.toko.maju.service.mapper.PurchaseListMapper;

/**
 * Service for executing complex queries for PurchaseList entities in the database.
 * The main input is a {@link PurchaseListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseListDTO} or a {@link Page} of {@link PurchaseListDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseListQueryService extends QueryService<PurchaseList> {

    private final Logger log = LoggerFactory.getLogger(PurchaseListQueryService.class);

    private final PurchaseListRepository purchaseListRepository;

    private final PurchaseListMapper purchaseListMapper;

    private final PurchaseListSearchRepository purchaseListSearchRepository;

    public PurchaseListQueryService(PurchaseListRepository purchaseListRepository, PurchaseListMapper purchaseListMapper, PurchaseListSearchRepository purchaseListSearchRepository) {
        this.purchaseListRepository = purchaseListRepository;
        this.purchaseListMapper = purchaseListMapper;
        this.purchaseListSearchRepository = purchaseListSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseListDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseListDTO> findByCriteria(PurchaseListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchaseList> specification = createSpecification(criteria);
        return purchaseListMapper.toDto(purchaseListRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PurchaseListDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseListDTO> findByCriteria(PurchaseListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchaseList> specification = createSpecification(criteria);
        return purchaseListRepository.findAll(specification, page)
            .map(purchaseListMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseListCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchaseList> specification = createSpecification(criteria);
        return purchaseListRepository.count(specification);
    }

    /**
     * Function to convert PurchaseListCriteria to a {@link Specification}
     */
    private Specification<PurchaseList> createSpecification(PurchaseListCriteria criteria) {
        Specification<PurchaseList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseList_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), PurchaseList_.barcode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PurchaseList_.name));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), PurchaseList_.unit));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), PurchaseList_.unitPrice));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), PurchaseList_.quantity));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), PurchaseList_.total));
            }
            if (criteria.getPurchaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getPurchaseId(),
                    root -> root.join(PurchaseList_.purchase, JoinType.LEFT).get(Purchase_.id)));
            }
        }
        return specification;
    }
}
