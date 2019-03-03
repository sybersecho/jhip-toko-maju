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

import com.toko.maju.domain.Product;
import com.toko.maju.domain.*; // for static metamodels
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.service.dto.ProductCriteria;
import com.toko.maju.service.dto.ProductDTO;
import com.toko.maju.service.mapper.ProductMapper;

/**
 * Service for executing complex queries for Product entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO}
 * which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

	private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

	private final ProductRepository productRepository;

	private final ProductMapper productMapper;

	private final ProductSearchRepository productSearchRepository;

	public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper,
			ProductSearchRepository productSearchRepository) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
		this.productSearchRepository = productSearchRepository;
	}

	/**
	 * Return a {@link List} of {@link ProductDTO} which matches the criteria from
	 * the database
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<Product> specification = createSpecification(criteria);
		return productMapper.toDto(productRepository.findAll(specification));
	}

	/**
	 * Return a {@link Page} of {@link ProductDTO} which matches the criteria from
	 * the database
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<Product> specification = createSpecification(criteria);
		return productRepository.findAll(specification, page).map(productMapper::toDto);
	}

	/**
	 * Return a {@link Page} of {@link ProductDTO} which matches the criteria from
	 * the database
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ProductDTO> findByFewCriteria(ProductCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<Product> specification = createOrSpecification(criteria);
		return productRepository.findAll(specification, page).map(productMapper::toDto);
	}

	/**
	 * Return the number of matching entities in the database
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ProductCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<Product> specification = createSpecification(criteria);
		return productRepository.count(specification);
	}

	/**
	 * Function to convert ProductCriteria to a {@link Specification}
	 */
	private Specification<Product> createSpecification(ProductCriteria criteria) {
		Specification<Product> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
			}
			if (criteria.getBarcode() != null) {
				specification = specification.and(buildStringSpecification(criteria.getBarcode(), Product_.barcode));
			}
			if (criteria.getName() != null) {
				specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
			}
			if (criteria.getUnit() != null) {
				specification = specification.and(buildSpecification(criteria.getUnit(), Product_.unit));
			}
			if (criteria.getWarehousePrice() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getWarehousePrice(), Product_.warehousePrice));
			}
			if (criteria.getUnitPrice() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), Product_.unitPrice));
			}
			if (criteria.getSellingPrice() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getSellingPrice(), Product_.sellingPrice));
			}
			if (criteria.getStock() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getStock(), Product_.stock));
			}
			if (criteria.getSupplierId() != null) {
				specification = specification.and(buildSpecification(criteria.getSupplierId(),
						root -> root.join(Product_.supplier, JoinType.LEFT).get(Supplier_.id)));
			}
			if (criteria.getSupplierName() != null) {
				specification = specification.and(buildSpecification(criteria.getSupplierName(),
						root -> root.join(Product_.supplier, JoinType.LEFT).get(Supplier_.name)));
			}
		}
		return specification;
	}

	/**
	 * Function to convert ProductCriteria to a {@link Specification}
	 */
	private Specification<Product> createOrSpecification(ProductCriteria criteria) {
		Specification<Product> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.or(buildSpecification(criteria.getId(), Product_.id));
			}
			if (criteria.getBarcode() != null) {
				specification = specification.or(buildStringSpecification(criteria.getBarcode(), Product_.barcode));
			}
			if (criteria.getName() != null) {
				specification = specification.or(buildStringSpecification(criteria.getName(), Product_.name));
			}
			if (criteria.getUnit() != null) {
				specification = specification.or(buildSpecification(criteria.getUnit(), Product_.unit));
			}
			if (criteria.getWarehousePrice() != null) {
				specification = specification
						.or(buildRangeSpecification(criteria.getWarehousePrice(), Product_.warehousePrice));
			}
			if (criteria.getUnitPrice() != null) {
				specification = specification.or(buildRangeSpecification(criteria.getUnitPrice(), Product_.unitPrice));
			}
			if (criteria.getSellingPrice() != null) {
				specification = specification
						.or(buildRangeSpecification(criteria.getSellingPrice(), Product_.sellingPrice));
			}
			if (criteria.getStock() != null) {
				specification = specification.or(buildRangeSpecification(criteria.getStock(), Product_.stock));
			}
			if (criteria.getSupplierId() != null) {
				specification = specification.or(buildSpecification(criteria.getSupplierId(),
						root -> root.join(Product_.supplier, JoinType.LEFT).get(Supplier_.id)));
			}
			if (criteria.getSupplierName() != null) {
				specification = specification.or(buildSpecification(criteria.getSupplierName(),
						root -> root.join(Product_.supplier, JoinType.LEFT).get(Supplier_.name)));
			}
		}
		return specification;
	}
}
