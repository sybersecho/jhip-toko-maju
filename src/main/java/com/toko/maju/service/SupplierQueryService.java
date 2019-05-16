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

import com.toko.maju.domain.Supplier;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.SupplierRepository;
import com.toko.maju.repository.search.SupplierSearchRepository;
import com.toko.maju.service.dto.SupplierCriteria;
import com.toko.maju.service.dto.SupplierDTO;
import com.toko.maju.service.mapper.SupplierMapper;

/**
 * Service for executing complex queries for Supplier entities in the database.
 * The main input is a {@link SupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SupplierDTO} or a {@link Page} of {@link SupplierDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierQueryService extends QueryService<Supplier> {

    private final Logger log = LoggerFactory.getLogger(SupplierQueryService.class);

    private final SupplierRepository supplierRepository;

    private final SupplierMapper supplierMapper;

    private final SupplierSearchRepository supplierSearchRepository;

    public SupplierQueryService(SupplierRepository supplierRepository, SupplierMapper supplierMapper, SupplierSearchRepository supplierSearchRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.supplierSearchRepository = supplierSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SupplierDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierDTO> findByCriteria(SupplierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierMapper.toDto(supplierRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SupplierDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierDTO> findByCriteria(SupplierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.findAll(specification, page)
            .map(supplierMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.count(specification);
    }

    /**
     * Function to convert SupplierCriteria to a {@link Specification}
     */
    private Specification<Supplier> createSpecification(SupplierCriteria criteria) {
        Specification<Supplier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.or(buildSpecification(criteria.getId(), Supplier_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getName(), Supplier_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.or(buildStringSpecification(criteria.getCode(), Supplier_.code));
            }
            if (criteria.getAddress() != null) {
                specification = specification.or(buildStringSpecification(criteria.getAddress(), Supplier_.address));
            }
            if (criteria.getNoTelp() != null) {
                specification = specification.or(buildStringSpecification(criteria.getNoTelp(), Supplier_.noTelp));
            }
            if (criteria.getBankAccount() != null) {
                specification = specification.or(buildStringSpecification(criteria.getBankAccount(), Supplier_.bankAccount));
            }
            if (criteria.getBankName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getBankName(), Supplier_.bankName));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(Supplier_.products, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
