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

import com.toko.maju.domain.DuePayment;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.DuePaymentRepository;
import com.toko.maju.repository.search.DuePaymentSearchRepository;
import com.toko.maju.service.dto.DuePaymentCriteria;
import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.mapper.DuePaymentMapper;

/**
 * Service for executing complex queries for DuePayment entities in the database.
 * The main input is a {@link DuePaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DuePaymentDTO} or a {@link Page} of {@link DuePaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DuePaymentQueryService extends QueryService<DuePayment> {

    private final Logger log = LoggerFactory.getLogger(DuePaymentQueryService.class);

    private final DuePaymentRepository duePaymentRepository;

    private final DuePaymentMapper duePaymentMapper;

    private final DuePaymentSearchRepository duePaymentSearchRepository;

    public DuePaymentQueryService(DuePaymentRepository duePaymentRepository, DuePaymentMapper duePaymentMapper, DuePaymentSearchRepository duePaymentSearchRepository) {
        this.duePaymentRepository = duePaymentRepository;
        this.duePaymentMapper = duePaymentMapper;
        this.duePaymentSearchRepository = duePaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DuePaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DuePaymentDTO> findByCriteria(DuePaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DuePayment> specification = createSpecification(criteria);
        return duePaymentMapper.toDto(duePaymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DuePaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DuePaymentDTO> findByCriteria(DuePaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DuePayment> specification = createSpecification(criteria);
        return duePaymentRepository.findAll(specification, page)
            .map(duePaymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DuePaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DuePayment> specification = createSpecification(criteria);
        return duePaymentRepository.count(specification);
    }

    /**
     * Function to convert DuePaymentCriteria to a {@link Specification}
     */
    private Specification<DuePayment> createSpecification(DuePaymentCriteria criteria) {
        Specification<DuePayment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DuePayment_.id));
            }
            if (criteria.getRemainingPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemainingPayment(), DuePayment_.remainingPayment));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), DuePayment_.createdDate));
            }
            if (criteria.getSettled() != null) {
                specification = specification.and(buildSpecification(criteria.getSettled(), DuePayment_.settled));
            }
            if (criteria.getPaid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaid(), DuePayment_.paid));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(DuePayment_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getSaleId() != null) {
                specification = specification.and(buildSpecification(criteria.getSaleId(),
                    root -> root.join(DuePayment_.sale, JoinType.LEFT).get(SaleTransactions_.id)));
            }
        }
        return specification;
    }
}
