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

import com.toko.maju.domain.StockOrderProcess;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.StockOrderProcessRepository;
import com.toko.maju.repository.search.StockOrderProcessSearchRepository;
import com.toko.maju.service.dto.StockOrderProcessCriteria;
import com.toko.maju.service.dto.StockOrderProcessDTO;
import com.toko.maju.service.mapper.StockOrderProcessMapper;

/**
 * Service for executing complex queries for StockOrderProcess entities in the database.
 * The main input is a {@link StockOrderProcessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockOrderProcessDTO} or a {@link Page} of {@link StockOrderProcessDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockOrderProcessQueryService extends QueryService<StockOrderProcess> {

    private final Logger log = LoggerFactory.getLogger(StockOrderProcessQueryService.class);

    private final StockOrderProcessRepository stockOrderProcessRepository;

    private final StockOrderProcessMapper stockOrderProcessMapper;

    private final StockOrderProcessSearchRepository stockOrderProcessSearchRepository;

    public StockOrderProcessQueryService(StockOrderProcessRepository stockOrderProcessRepository, StockOrderProcessMapper stockOrderProcessMapper, StockOrderProcessSearchRepository stockOrderProcessSearchRepository) {
        this.stockOrderProcessRepository = stockOrderProcessRepository;
        this.stockOrderProcessMapper = stockOrderProcessMapper;
        this.stockOrderProcessSearchRepository = stockOrderProcessSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StockOrderProcessDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockOrderProcessDTO> findByCriteria(StockOrderProcessCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockOrderProcess> specification = createSpecification(criteria);
        return stockOrderProcessMapper.toDto(stockOrderProcessRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockOrderProcessDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockOrderProcessDTO> findByCriteria(StockOrderProcessCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockOrderProcess> specification = createSpecification(criteria);
        return stockOrderProcessRepository.findAll(specification, page)
            .map(stockOrderProcessMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockOrderProcessCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockOrderProcess> specification = createSpecification(criteria);
        return stockOrderProcessRepository.count(specification);
    }

    /**
     * Function to convert StockOrderProcessCriteria to a {@link Specification}
     */
    private Specification<StockOrderProcess> createSpecification(StockOrderProcessCriteria criteria) {
        Specification<StockOrderProcess> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StockOrderProcess_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), StockOrderProcess_.barcode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StockOrderProcess_.name));
            }
            if (criteria.getQuantityRequest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityRequest(), StockOrderProcess_.quantityRequest));
            }
            if (criteria.getStockInHand() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStockInHand(), StockOrderProcess_.stockInHand));
            }
            if (criteria.getQuantityApprove() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityApprove(), StockOrderProcess_.quantityApprove));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), StockOrderProcess_.createdDate));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(StockOrderProcess_.creator, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
