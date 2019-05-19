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

import com.toko.maju.domain.StockOrderReceive;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.StockOrderReceiveRepository;
import com.toko.maju.repository.search.StockOrderReceiveSearchRepository;
import com.toko.maju.service.dto.StockOrderReceiveCriteria;
import com.toko.maju.service.dto.StockOrderReceiveDTO;
import com.toko.maju.service.mapper.StockOrderReceiveMapper;

/**
 * Service for executing complex queries for StockOrderReceive entities in the database.
 * The main input is a {@link StockOrderReceiveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockOrderReceiveDTO} or a {@link Page} of {@link StockOrderReceiveDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockOrderReceiveQueryService extends QueryService<StockOrderReceive> {

    private final Logger log = LoggerFactory.getLogger(StockOrderReceiveQueryService.class);

    private final StockOrderReceiveRepository stockOrderReceiveRepository;

    private final StockOrderReceiveMapper stockOrderReceiveMapper;

    private final StockOrderReceiveSearchRepository stockOrderReceiveSearchRepository;

    public StockOrderReceiveQueryService(StockOrderReceiveRepository stockOrderReceiveRepository, StockOrderReceiveMapper stockOrderReceiveMapper, StockOrderReceiveSearchRepository stockOrderReceiveSearchRepository) {
        this.stockOrderReceiveRepository = stockOrderReceiveRepository;
        this.stockOrderReceiveMapper = stockOrderReceiveMapper;
        this.stockOrderReceiveSearchRepository = stockOrderReceiveSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StockOrderReceiveDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockOrderReceiveDTO> findByCriteria(StockOrderReceiveCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockOrderReceive> specification = createSpecification(criteria);
        return stockOrderReceiveMapper.toDto(stockOrderReceiveRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockOrderReceiveDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockOrderReceiveDTO> findByCriteria(StockOrderReceiveCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockOrderReceive> specification = createSpecification(criteria);
        return stockOrderReceiveRepository.findAll(specification, page)
            .map(stockOrderReceiveMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockOrderReceiveCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockOrderReceive> specification = createSpecification(criteria);
        return stockOrderReceiveRepository.count(specification);
    }

    /**
     * Function to convert StockOrderReceiveCriteria to a {@link Specification}
     */
    private Specification<StockOrderReceive> createSpecification(StockOrderReceiveCriteria criteria) {
        Specification<StockOrderReceive> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StockOrderReceive_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), StockOrderReceive_.barcode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StockOrderReceive_.name));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), StockOrderReceive_.quantity));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), StockOrderReceive_.createdDate));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(StockOrderReceive_.creator, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
