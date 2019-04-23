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

import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.SaleTransactionsRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.service.mapper.SaleTransactionsMapper;

/**
 * Service for executing complex queries for SaleTransactions entities in the database.
 * The main input is a {@link SaleTransactionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SaleTransactionsDTO} or a {@link Page} of {@link SaleTransactionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaleTransactionsQueryService extends QueryService<SaleTransactions> {

    private final Logger log = LoggerFactory.getLogger(SaleTransactionsQueryService.class);

    private final SaleTransactionsRepository saleTransactionsRepository;

    private final SaleTransactionsMapper saleTransactionsMapper;

    private final SaleTransactionsSearchRepository saleTransactionsSearchRepository;

    public SaleTransactionsQueryService(SaleTransactionsRepository saleTransactionsRepository, SaleTransactionsMapper saleTransactionsMapper, SaleTransactionsSearchRepository saleTransactionsSearchRepository) {
        this.saleTransactionsRepository = saleTransactionsRepository;
        this.saleTransactionsMapper = saleTransactionsMapper;
        this.saleTransactionsSearchRepository = saleTransactionsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SaleTransactionsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SaleTransactionsDTO> findByCriteria(SaleTransactionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SaleTransactions> specification = createSpecification(criteria);
        return saleTransactionsMapper.toDto(saleTransactionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SaleTransactionsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaleTransactionsDTO> findByCriteria(SaleTransactionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SaleTransactions> specification = createSpecification(criteria);
        return saleTransactionsRepository.findAll(specification, page)
            .map(saleTransactionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SaleTransactionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SaleTransactions> specification = createSpecification(criteria);
        return saleTransactionsRepository.count(specification);
    }

    /**
     * Function to convert SaleTransactionsCriteria to a {@link Specification}
     */
    private Specification<SaleTransactions> createSpecification(SaleTransactionsCriteria criteria) {
        Specification<SaleTransactions> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SaleTransactions_.id));
            }
            if (criteria.getNoInvoice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoInvoice(), SaleTransactions_.noInvoice));
            }
            if (criteria.getDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscount(), SaleTransactions_.discount));
            }
            if (criteria.getTotalPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPayment(), SaleTransactions_.totalPayment));
            }
            if (criteria.getRemainingPayment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemainingPayment(), SaleTransactions_.remainingPayment));
            }
            if (criteria.getPaid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaid(), SaleTransactions_.paid));
            }
            if (criteria.getSaleDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaleDate(), SaleTransactions_.saleDate));
            }
            if (criteria.getSettled() != null) {
                specification = specification.and(buildSpecification(criteria.getSettled(), SaleTransactions_.settled));
            }
            if (criteria.getItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemsId(),
                    root -> root.join(SaleTransactions_.items, JoinType.LEFT).get(SaleItem_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(SaleTransactions_.customer, JoinType.LEFT).get(Customer_.id)));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(SaleTransactions_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getDuePaymentId() != null) {
                specification = specification.and(buildSpecification(criteria.getDuePaymentId(),
                    root -> root.join(SaleTransactions_.duePayments, JoinType.LEFT).get(DuePayment_.id)));
            }
        }
        return specification;
    }
}
