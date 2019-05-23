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

import com.toko.maju.domain.GeraiConfig;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.GeraiConfigRepository;
import com.toko.maju.repository.search.GeraiConfigSearchRepository;
import com.toko.maju.service.dto.GeraiConfigCriteria;
import com.toko.maju.service.dto.GeraiConfigDTO;
import com.toko.maju.service.mapper.GeraiConfigMapper;

/**
 * Service for executing complex queries for GeraiConfig entities in the database.
 * The main input is a {@link GeraiConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeraiConfigDTO} or a {@link Page} of {@link GeraiConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeraiConfigQueryService extends QueryService<GeraiConfig> {

    private final Logger log = LoggerFactory.getLogger(GeraiConfigQueryService.class);

    private final GeraiConfigRepository geraiConfigRepository;

    private final GeraiConfigMapper geraiConfigMapper;

    private final GeraiConfigSearchRepository geraiConfigSearchRepository;

    public GeraiConfigQueryService(GeraiConfigRepository geraiConfigRepository, GeraiConfigMapper geraiConfigMapper, GeraiConfigSearchRepository geraiConfigSearchRepository) {
        this.geraiConfigRepository = geraiConfigRepository;
        this.geraiConfigMapper = geraiConfigMapper;
        this.geraiConfigSearchRepository = geraiConfigSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GeraiConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeraiConfigDTO> findByCriteria(GeraiConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GeraiConfig> specification = createSpecification(criteria);
        return geraiConfigMapper.toDto(geraiConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeraiConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeraiConfigDTO> findByCriteria(GeraiConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GeraiConfig> specification = createSpecification(criteria);
        return geraiConfigRepository.findAll(specification, page)
            .map(geraiConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeraiConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GeraiConfig> specification = createSpecification(criteria);
        return geraiConfigRepository.count(specification);
    }

    /**
     * Function to convert GeraiConfigCriteria to a {@link Specification}
     */
    private Specification<GeraiConfig> createSpecification(GeraiConfigCriteria criteria) {
        Specification<GeraiConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GeraiConfig_.id));
            }
            if (criteria.getCodeGerai() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeGerai(), GeraiConfig_.codeGerai));
            }
            if (criteria.getNameGerai() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameGerai(), GeraiConfig_.nameGerai));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), GeraiConfig_.password));
            }
            if (criteria.getUrlToko() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlToko(), GeraiConfig_.urlToko));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), GeraiConfig_.activated));
            }
        }
        return specification;
    }
}
