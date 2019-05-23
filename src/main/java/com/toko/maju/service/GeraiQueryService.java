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

import com.toko.maju.domain.Gerai;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.GeraiRepository;
import com.toko.maju.repository.search.GeraiSearchRepository;
import com.toko.maju.service.dto.GeraiCriteria;
import com.toko.maju.service.dto.GeraiDTO;
import com.toko.maju.service.mapper.GeraiMapper;

/**
 * Service for executing complex queries for Gerai entities in the database.
 * The main input is a {@link GeraiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeraiDTO} or a {@link Page} of {@link GeraiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeraiQueryService extends QueryService<Gerai> {

    private final Logger log = LoggerFactory.getLogger(GeraiQueryService.class);

    private final GeraiRepository geraiRepository;

    private final GeraiMapper geraiMapper;

    private final GeraiSearchRepository geraiSearchRepository;

    public GeraiQueryService(GeraiRepository geraiRepository, GeraiMapper geraiMapper, GeraiSearchRepository geraiSearchRepository) {
        this.geraiRepository = geraiRepository;
        this.geraiMapper = geraiMapper;
        this.geraiSearchRepository = geraiSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GeraiDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeraiDTO> findByCriteria(GeraiCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gerai> specification = createSpecification(criteria);
        return geraiMapper.toDto(geraiRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeraiDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeraiDTO> findByCriteria(GeraiCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gerai> specification = createSpecification(criteria);
        return geraiRepository.findAll(specification, page)
            .map(geraiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeraiCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gerai> specification = createSpecification(criteria);
        return geraiRepository.count(specification);
    }

    /**
     * Function to convert GeraiCriteria to a {@link Specification}
     */
    private Specification<Gerai> createSpecification(GeraiCriteria criteria) {
        Specification<Gerai> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Gerai_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Gerai_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Gerai_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Gerai_.location));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Gerai_.password));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Gerai_.createdDate));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(Gerai_.creator, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
