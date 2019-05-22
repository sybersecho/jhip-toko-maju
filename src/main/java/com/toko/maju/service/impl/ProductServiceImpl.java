package com.toko.maju.service.impl;

import com.toko.maju.domain.Supplier;
import com.toko.maju.domain.Unit;
import com.toko.maju.repository.SupplierRepository;
import com.toko.maju.repository.UnitRepository;
import com.toko.maju.service.ProductService;
import com.toko.maju.domain.Product;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.service.dto.ProductDTO;
import com.toko.maju.service.mapper.ProductMapper;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.vm.ImportProductVM;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    @Autowired
    private final UnitRepository unitRepository = null;

    @Autowired
    private final SupplierRepository supplierRepository = null;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        productSearchRepository.save(product);
        return result;
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
    }


    /**
     * Get one product by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id)
            .map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    /**
     * Search for the product corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productSearchRepository.search(queryStringQuery(query), pageable)
            .map(productMapper::toDto);
    }

    @Override
    public List<ProductDTO> importProduct(List<ImportProductVM> importProductVMs) {
        List<Product> newProducts = new ArrayList<>();
        List<Product> finalNewProducts = newProducts;
        importProductVMs.forEach(p -> {
            Product existingProduct = productRepository.findByBarcode(p.getBarcode());

            if (existingProduct == null) {
                Product newProduct = new Product();
                newProduct.setStock(p.getStock());
                newProduct.setBarcode(p.getBarcode());
                newProduct.setName(p.getProductName());
                newProduct.setSellingPrice(p.getSalePrice());
                newProduct.setUnitPrice(p.getUnitPrice());
                newProduct.setWarehousePrice(p.getWarehousePrice());

                Unit unit = unitRepository.findByName(p.getUnit());

                if (unit != null) {
                    newProduct.setUnit(unit);
                }

                Supplier supplier = supplierRepository.findByCode(p.getSupplierCode());
                if (supplier != null) {
                    newProduct.setSupplier(supplier);
                }

                finalNewProducts.add(newProduct);

            }

        });
//
//        if(newProducts.isEmpty()){
//            throw new BadRequestException("sdf");
//        }
        if (!newProducts.isEmpty()) {
            newProducts = productRepository.saveAll(newProducts);
            productSearchRepository.saveAll(newProducts);
        }
        return productMapper.toDto(newProducts);
    }
}
