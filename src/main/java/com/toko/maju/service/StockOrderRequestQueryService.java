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

import com.toko.maju.domain.StockOrderRequest;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.StockOrderRequestRepository;
import com.toko.maju.repository.search.StockOrderRequestSearchRepository;
import com.toko.maju.service.dto.StockOrderRequestCriteria;
import com.toko.maju.service.dto.StockOrderRequestDTO;
import com.toko.maju.service.mapper.StockOrderRequestMapper;

/**
 * Service for executing complex queries for StockOrderRequest entities in the database.
 * The main input is a {@link StockOrderRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockOrderRequestDTO} or a {@link Page} of {@link StockOrderRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockOrderRequestQueryService extends QueryService<StockOrderRequest> {

    private final Logger log = LoggerFactory.getLogger(StockOrderRequestQueryService.class);

    private final StockOrderRequestRepository stockOrderRequestRepository;

    private final StockOrderRequestMapper stockOrderRequestMapper;

    private final StockOrderRequestSearchRepository stockOrderRequestSearchRepository;

    public StockOrderRequestQueryService(StockOrderRequestRepository stockOrderRequestRepository, StockOrderRequestMapper stockOrderRequestMapper, StockOrderRequestSearchRepository stockOrderRequestSearchRepository) {
        this.stockOrderRequestRepository = stockOrderRequestRepository;
        this.stockOrderRequestMapper = stockOrderRequestMapper;
        this.stockOrderRequestSearchRepository = stockOrderRequestSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StockOrderRequestDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockOrderRequestDTO> findByCriteria(StockOrderRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockOrderRequest> specification = createSpecification(criteria);
        return stockOrderRequestMapper.toDto(stockOrderRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockOrderRequestDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockOrderRequestDTO> findByCriteria(StockOrderRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockOrderRequest> specification = createSpecification(criteria);
        return stockOrderRequestRepository.findAll(specification, page)
            .map(stockOrderRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockOrderRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockOrderRequest> specification = createSpecification(criteria);
        return stockOrderRequestRepository.count(specification);
    }

    /**
     * Function to convert StockOrderRequestCriteria to a {@link Specification}
     */
    private Specification<StockOrderRequest> createSpecification(StockOrderRequestCriteria criteria) {
        Specification<StockOrderRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StockOrderRequest_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), StockOrderRequest_.barcode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StockOrderRequest_.name));
            }
            if (criteria.getUnitName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnitName(), StockOrderRequest_.unitName));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), StockOrderRequest_.unitPrice));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), StockOrderRequest_.quantity));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), StockOrderRequest_.totalPrice));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), StockOrderRequest_.createdDate));
            }
            if (criteria.getStockOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockOrderId(),
                    root -> root.join(StockOrderRequest_.stockOrder, JoinType.LEFT).get(StockOrder_.id)));
            }
        }
        return specification;
    }
}
