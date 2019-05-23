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

import com.toko.maju.domain.GeraiUpdateHistory;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.GeraiUpdateHistoryRepository;
import com.toko.maju.repository.search.GeraiUpdateHistorySearchRepository;
import com.toko.maju.service.dto.GeraiUpdateHistoryCriteria;
import com.toko.maju.service.dto.GeraiUpdateHistoryDTO;
import com.toko.maju.service.mapper.GeraiUpdateHistoryMapper;

/**
 * Service for executing complex queries for GeraiUpdateHistory entities in the database.
 * The main input is a {@link GeraiUpdateHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeraiUpdateHistoryDTO} or a {@link Page} of {@link GeraiUpdateHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeraiUpdateHistoryQueryService extends QueryService<GeraiUpdateHistory> {

    private final Logger log = LoggerFactory.getLogger(GeraiUpdateHistoryQueryService.class);

    private final GeraiUpdateHistoryRepository geraiUpdateHistoryRepository;

    private final GeraiUpdateHistoryMapper geraiUpdateHistoryMapper;

    private final GeraiUpdateHistorySearchRepository geraiUpdateHistorySearchRepository;

    public GeraiUpdateHistoryQueryService(GeraiUpdateHistoryRepository geraiUpdateHistoryRepository, GeraiUpdateHistoryMapper geraiUpdateHistoryMapper, GeraiUpdateHistorySearchRepository geraiUpdateHistorySearchRepository) {
        this.geraiUpdateHistoryRepository = geraiUpdateHistoryRepository;
        this.geraiUpdateHistoryMapper = geraiUpdateHistoryMapper;
        this.geraiUpdateHistorySearchRepository = geraiUpdateHistorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link GeraiUpdateHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeraiUpdateHistoryDTO> findByCriteria(GeraiUpdateHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GeraiUpdateHistory> specification = createSpecification(criteria);
        return geraiUpdateHistoryMapper.toDto(geraiUpdateHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeraiUpdateHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeraiUpdateHistoryDTO> findByCriteria(GeraiUpdateHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GeraiUpdateHistory> specification = createSpecification(criteria);
        return geraiUpdateHistoryRepository.findAll(specification, page)
            .map(geraiUpdateHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeraiUpdateHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GeraiUpdateHistory> specification = createSpecification(criteria);
        return geraiUpdateHistoryRepository.count(specification);
    }

    /**
     * Function to convert GeraiUpdateHistoryCriteria to a {@link Specification}
     */
    private Specification<GeraiUpdateHistory> createSpecification(GeraiUpdateHistoryCriteria criteria) {
        Specification<GeraiUpdateHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GeraiUpdateHistory_.id));
            }
            if (criteria.getLastSaleId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastSaleId(), GeraiUpdateHistory_.lastSaleId));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), GeraiUpdateHistory_.createdDate));
            }
            if (criteria.getSaleDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaleDate(), GeraiUpdateHistory_.saleDate));
            }
        }
        return specification;
    }
}
