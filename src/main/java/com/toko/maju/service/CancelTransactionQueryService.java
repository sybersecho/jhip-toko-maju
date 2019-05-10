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

import com.toko.maju.domain.CancelTransaction;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.CancelTransactionRepository;
import com.toko.maju.repository.search.CancelTransactionSearchRepository;
import com.toko.maju.service.dto.CancelTransactionCriteria;
import com.toko.maju.service.dto.CancelTransactionDTO;
import com.toko.maju.service.mapper.CancelTransactionMapper;

/**
 * Service for executing complex queries for CancelTransaction entities in the database.
 * The main input is a {@link CancelTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CancelTransactionDTO} or a {@link Page} of {@link CancelTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CancelTransactionQueryService extends QueryService<CancelTransaction> {

    private final Logger log = LoggerFactory.getLogger(CancelTransactionQueryService.class);

    private final CancelTransactionRepository cancelTransactionRepository;

    private final CancelTransactionMapper cancelTransactionMapper;

    private final CancelTransactionSearchRepository cancelTransactionSearchRepository;

    public CancelTransactionQueryService(CancelTransactionRepository cancelTransactionRepository, CancelTransactionMapper cancelTransactionMapper, CancelTransactionSearchRepository cancelTransactionSearchRepository) {
        this.cancelTransactionRepository = cancelTransactionRepository;
        this.cancelTransactionMapper = cancelTransactionMapper;
        this.cancelTransactionSearchRepository = cancelTransactionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CancelTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CancelTransactionDTO> findByCriteria(CancelTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CancelTransaction> specification = createSpecification(criteria);
        return cancelTransactionMapper.toDto(cancelTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CancelTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CancelTransactionDTO> findByCriteria(CancelTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CancelTransaction> specification = createSpecification(criteria);
        return cancelTransactionRepository.findAll(specification, page)
            .map(cancelTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CancelTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CancelTransaction> specification = createSpecification(criteria);
        return cancelTransactionRepository.count(specification);
    }

    /**
     * Function to convert CancelTransactionCriteria to a {@link Specification}
     */
    private Specification<CancelTransaction> createSpecification(CancelTransactionCriteria criteria) {
        Specification<CancelTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CancelTransaction_.id));
            }
            if (criteria.getNoInvoice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoInvoice(), CancelTransaction_.noInvoice));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), CancelTransaction_.cancelDate));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), CancelTransaction_.note));
            }
            if (criteria.getSaleTransactionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getSaleTransactionsId(),
                    root -> root.join(CancelTransaction_.saleTransactions, JoinType.LEFT).get(SaleTransactions_.id)));
            }
        }
        return specification;
    }
}
