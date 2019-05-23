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

import com.toko.maju.domain.GeraiTransaction;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.GeraiTransactionRepository;
import com.toko.maju.repository.search.GeraiTransactionSearchRepository;
import com.toko.maju.service.dto.GeraiTransactionCriteria;
import com.toko.maju.service.dto.GeraiTransactionDTO;
import com.toko.maju.service.mapper.GeraiTransactionMapper;

/**
 * Service for executing complex queries for GeraiTransaction entities in the database.
 * The main input is a {@link GeraiTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeraiTransactionDTO} or a {@link Page} of {@link GeraiTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeraiTransactionQueryService extends QueryService<GeraiTransaction> {

    private final Logger log = LoggerFactory.getLogger(GeraiTransactionQueryService.class);

    private final GeraiTransactionRepository geraiTransactionRepository;

    private final GeraiTransactionMapper geraiTransactionMapper;

    private final GeraiTransactionSearchRepository geraiTransactionSearchRepository;

    public GeraiTransactionQueryService(GeraiTransactionRepository geraiTransactionRepository, GeraiTransactionMapper geraiTransactionMapper, GeraiTransactionSearchRepository geraiTransactionSearchRepository) {
        this.geraiTransactionRepository = geraiTransactionRepository;
        this.geraiTransactionMapper = geraiTransactionMapper;
        this.geraiTransactionSearchRepository = geraiTransactionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GeraiTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeraiTransactionDTO> findByCriteria(GeraiTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GeraiTransaction> specification = createSpecification(criteria);
        return geraiTransactionMapper.toDto(geraiTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeraiTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeraiTransactionDTO> findByCriteria(GeraiTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GeraiTransaction> specification = createSpecification(criteria);
        return geraiTransactionRepository.findAll(specification, page)
            .map(geraiTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeraiTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GeraiTransaction> specification = createSpecification(criteria);
        return geraiTransactionRepository.count(specification);
    }

    /**
     * Function to convert GeraiTransactionCriteria to a {@link Specification}
     */
    private Specification<GeraiTransaction> createSpecification(GeraiTransactionCriteria criteria) {
        Specification<GeraiTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GeraiTransaction_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), GeraiTransaction_.barcode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), GeraiTransaction_.name));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), GeraiTransaction_.quantity));
            }
            if (criteria.getCurrentStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentStock(), GeraiTransaction_.currentStock));
            }
            if (criteria.getReceivedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceivedDate(), GeraiTransaction_.receivedDate));
            }
            if (criteria.getGeraiId() != null) {
                specification = specification.and(buildSpecification(criteria.getGeraiId(),
                    root -> root.join(GeraiTransaction_.gerai, JoinType.LEFT).get(Gerai_.id)));
            }
        }
        return specification;
    }
}
