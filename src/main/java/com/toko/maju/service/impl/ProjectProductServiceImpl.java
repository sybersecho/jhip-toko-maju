package com.toko.maju.service.impl;

import com.toko.maju.service.ProjectProductService;
import com.toko.maju.domain.ProjectProduct;
import com.toko.maju.repository.ProjectProductRepository;
import com.toko.maju.repository.search.ProjectProductSearchRepository;
import com.toko.maju.service.dto.ProjectProductDTO;
import com.toko.maju.service.mapper.ProjectProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProjectProduct.
 */
@Service
@Transactional
public class ProjectProductServiceImpl implements ProjectProductService {

	private final Logger log = LoggerFactory.getLogger(ProjectProductServiceImpl.class);

	private final ProjectProductRepository projectProductRepository;

	private final ProjectProductMapper projectProductMapper;

	private final ProjectProductSearchRepository projectProductSearchRepository;

	public ProjectProductServiceImpl(ProjectProductRepository projectProductRepository,
			ProjectProductMapper projectProductMapper, ProjectProductSearchRepository projectProductSearchRepository) {
		this.projectProductRepository = projectProductRepository;
		this.projectProductMapper = projectProductMapper;
		this.projectProductSearchRepository = projectProductSearchRepository;
	}

	/**
	 * Save a projectProduct.
	 *
	 * @param projectProductDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public ProjectProductDTO save(ProjectProductDTO projectProductDTO) {
		log.debug("Request to save ProjectProduct : {}", projectProductDTO);
		ProjectProduct projectProduct = projectProductMapper.toEntity(projectProductDTO);
		projectProduct = projectProductRepository.save(projectProduct);
		ProjectProductDTO result = projectProductMapper.toDto(projectProduct);
		projectProductSearchRepository.save(projectProduct);
		return result;
	}

	/**
	 * Get all the projectProducts.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ProjectProductDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ProjectProducts");
		return projectProductRepository.findAll(pageable).map(projectProductMapper::toDto);
	}

	/**
	 * Get one projectProduct by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<ProjectProductDTO> findOne(Long id) {
		log.debug("Request to get ProjectProduct : {}", id);
		return projectProductRepository.findById(id).map(projectProductMapper::toDto);
	}

	/**
	 * Delete the projectProduct by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete ProjectProduct : {}", id);
		projectProductRepository.deleteById(id);
		projectProductSearchRepository.deleteById(id);
	}

	/**
	 * Search for the projectProduct corresponding to the query.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ProjectProductDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of ProjectProducts for query {}", query);
		return projectProductSearchRepository.search(queryStringQuery(query), pageable)
				.map(projectProductMapper::toDto);
	}

	@Override
	public ProjectProductDTO batchSave(List<ProjectProductDTO> projectProductDTOs) {
		log.debug("Request to save ProjectProduct : ");
		projectProductRepository.saveAll(projectProductMapper.toEntity(projectProductDTOs));
		return projectProductDTOs.get(0);
	}
}
