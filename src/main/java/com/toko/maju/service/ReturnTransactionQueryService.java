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

import com.toko.maju.domain.ReturnTransaction;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.ReturnTransactionRepository;
import com.toko.maju.repository.search.ReturnTransactionSearchRepository;
import com.toko.maju.service.dto.ReturnTransactionCriteria;
import com.toko.maju.service.dto.ReturnTransactionDTO;
import com.toko.maju.service.mapper.ReturnTransactionMapper;

/**
 * Service for executing complex queries for ReturnTransaction entities in the database.
 * The main input is a {@link ReturnTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReturnTransactionDTO} or a {@link Page} of {@link ReturnTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReturnTransactionQueryService extends QueryService<ReturnTransaction> {

    private final Logger log = LoggerFactory.getLogger(ReturnTransactionQueryService.class);

    private final ReturnTransactionRepository returnTransactionRepository;

    private final ReturnTransactionMapper returnTransactionMapper;

    private final ReturnTransactionSearchRepository returnTransactionSearchRepository;

    public ReturnTransactionQueryService(ReturnTransactionRepository returnTransactionRepository, ReturnTransactionMapper returnTransactionMapper, ReturnTransactionSearchRepository returnTransactionSearchRepository) {
        this.returnTransactionRepository = returnTransactionRepository;
        this.returnTransactionMapper = returnTransactionMapper;
        this.returnTransactionSearchRepository = returnTransactionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReturnTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReturnTransactionDTO> findByCriteria(ReturnTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReturnTransaction> specification = createSpecification(criteria);
        return returnTransactionMapper.toDto(returnTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReturnTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReturnTransactionDTO> findByCriteria(ReturnTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReturnTransaction> specification = createSpecification(criteria);
        return returnTransactionRepository.findAll(specification, page)
            .map(returnTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReturnTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReturnTransaction> specification = createSpecification(criteria);
        return returnTransactionRepository.count(specification);
    }

    /**
     * Function to convert ReturnTransactionCriteria to a {@link Specification}
     */
    private Specification<ReturnTransaction> createSpecification(ReturnTransactionCriteria criteria) {
        Specification<ReturnTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ReturnTransaction_.id));
            }
            if (criteria.getCreated_date() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated_date(), ReturnTransaction_.created_date));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionType(), ReturnTransaction_.transactionType));
            }
            if (criteria.getTotalPriceReturn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPriceReturn(), ReturnTransaction_.totalPriceReturn));
            }
            if (criteria.getNoTransaction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoTransaction(), ReturnTransaction_.noTransaction));
            }
            if (criteria.getCashReturned() != null) {
                specification = specification.and(buildSpecification(criteria.getCashReturned(), ReturnTransaction_.cashReturned));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(ReturnTransaction_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(ReturnTransaction_.customer, JoinType.LEFT).get(Customer_.id)));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(ReturnTransaction_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
            if (criteria.getReturnItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getReturnItemId(),
                    root -> root.join(ReturnTransaction_.returnItems, JoinType.LEFT).get(ReturnItem_.id)));
            }
        }
        return specification;
    }
}
