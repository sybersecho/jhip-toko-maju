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

import com.toko.maju.domain.StockOrder;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.StockOrderRepository;
import com.toko.maju.repository.search.StockOrderSearchRepository;
import com.toko.maju.service.dto.StockOrderCriteria;
import com.toko.maju.service.dto.StockOrderDTO;
import com.toko.maju.service.mapper.StockOrderMapper;

/**
 * Service for executing complex queries for StockOrder entities in the database.
 * The main input is a {@link StockOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockOrderDTO} or a {@link Page} of {@link StockOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockOrderQueryService extends QueryService<StockOrder> {

    private final Logger log = LoggerFactory.getLogger(StockOrderQueryService.class);

    private final StockOrderRepository stockOrderRepository;

    private final StockOrderMapper stockOrderMapper;

    private final StockOrderSearchRepository stockOrderSearchRepository;

    public StockOrderQueryService(StockOrderRepository stockOrderRepository, StockOrderMapper stockOrderMapper, StockOrderSearchRepository stockOrderSearchRepository) {
        this.stockOrderRepository = stockOrderRepository;
        this.stockOrderMapper = stockOrderMapper;
        this.stockOrderSearchRepository = stockOrderSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StockOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockOrderDTO> findByCriteria(StockOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockOrder> specification = createSpecification(criteria);
        return stockOrderMapper.toDto(stockOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockOrderDTO> findByCriteria(StockOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockOrder> specification = createSpecification(criteria);
        return stockOrderRepository.findAll(specification, page)
            .map(stockOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockOrder> specification = createSpecification(criteria);
        return stockOrderRepository.count(specification);
    }

    /**
     * Function to convert StockOrderCriteria to a {@link Specification}
     */
    private Specification<StockOrder> createSpecification(StockOrderCriteria criteria) {
        Specification<StockOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StockOrder_.id));
            }
            if (criteria.getSiteLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSiteLocation(), StockOrder_.siteLocation));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), StockOrder_.createdDate));
            }
            if (criteria.getProcessed() != null) {
                specification = specification.and(buildSpecification(criteria.getProcessed(), StockOrder_.processed));
            }
            if (criteria.getProcessedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessedDate(), StockOrder_.processedDate));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(StockOrder_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getApprovalId() != null) {
                specification = specification.and(buildSpecification(criteria.getApprovalId(),
                    root -> root.join(StockOrder_.approval, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
