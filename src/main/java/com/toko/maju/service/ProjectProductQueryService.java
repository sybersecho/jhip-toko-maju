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

import com.toko.maju.domain.ProjectProduct;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.ProjectProductRepository;
import com.toko.maju.repository.search.ProjectProductSearchRepository;
import com.toko.maju.service.dto.ProjectProductCriteria;
import com.toko.maju.service.dto.ProjectProductDTO;
import com.toko.maju.service.mapper.ProjectProductMapper;

/**
 * Service for executing complex queries for ProjectProduct entities in the database.
 * The main input is a {@link ProjectProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProjectProductDTO} or a {@link Page} of {@link ProjectProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectProductQueryService extends QueryService<ProjectProduct> {

    private final Logger log = LoggerFactory.getLogger(ProjectProductQueryService.class);

    private final ProjectProductRepository projectProductRepository;

    private final ProjectProductMapper projectProductMapper;

    private final ProjectProductSearchRepository projectProductSearchRepository;

    public ProjectProductQueryService(ProjectProductRepository projectProductRepository, ProjectProductMapper projectProductMapper, ProjectProductSearchRepository projectProductSearchRepository) {
        this.projectProductRepository = projectProductRepository;
        this.projectProductMapper = projectProductMapper;
        this.projectProductSearchRepository = projectProductSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProjectProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectProductDTO> findByCriteria(ProjectProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProjectProduct> specification = createSpecification(criteria);
        return projectProductMapper.toDto(projectProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProjectProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectProductDTO> findByCriteria(ProjectProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProjectProduct> specification = createSpecification(criteria);
        return projectProductRepository.findAll(specification, page)
            .map(projectProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjectProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProjectProduct> specification = createSpecification(criteria);
        return projectProductRepository.count(specification);
    }

    /**
     * Function to convert ProjectProductCriteria to a {@link Specification}
     */
    private Specification<ProjectProduct> createSpecification(ProjectProductCriteria criteria) {
        Specification<ProjectProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProjectProduct_.id));
            }
            if (criteria.getSpecialPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpecialPrice(), ProjectProduct_.specialPrice));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(ProjectProduct_.product, JoinType.LEFT).get(Product_.id)));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(ProjectProduct_.project, JoinType.LEFT).get(Project_.id)));
            }
        }
        return specification;
    }
}
