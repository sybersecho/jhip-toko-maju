package com.toko.maju.web.rest;

import com.toko.maju.service.ProductService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.ProductDTO;
import com.toko.maju.service.dto.ProductCriteria;
import com.toko.maju.service.ProductQueryService;
import com.toko.maju.web.rest.vm.ExtractProductVM;
import com.toko.maju.web.rest.vm.ImportProductVM;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    private final ProductService productService;

    private final ProductQueryService productQueryService;

    public ProductResource(ProductService productService, ProductQueryService productQueryService) {
        this.productService = productService;
        this.productQueryService = productQueryService;
    }

    /**
     * POST  /products : Create a new product.
     *
     * @param productDTO the productDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productDTO, or with status 400 (Bad Request) if the product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /products : Updates an existing product.
     *
     * @param productDTO the productDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productDTO,
     * or with status 400 (Bad Request) if the productDTO is not valid,
     * or with status 500 (Internal Server Error) if the productDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/products")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to update Product : {}", productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(ProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Products by criteria: {}", criteria);
        Page<ProductDTO> page = productQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products-by")
    public ResponseEntity<List<ProductDTO>> getAllProductBy(ProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Products by criteria: {}", criteria);
        Page<ProductDTO> page = productQueryService.findByFewCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/products-by");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /products/count : count all the products.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/products/count")
    public ResponseEntity<Long> countProducts(ProductCriteria criteria) {
        log.debug("REST request to count Products by criteria: {}", criteria);
        return ResponseEntity.ok().body(productQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productDTO, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the productDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/products?query=:query : search for the product corresponding
     * to the query.
     *
     * @param query    the query of the product search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/products")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Products for query {}", query);
        Page<ProductDTO> page = productService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/products/extract-by-product/{id}")
    public ResponseEntity<ExtractProductVM> getProductExtract(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        Optional<ExtractProductVM> extractProductVM = createOptionalExtractProduct(productDTO);
        return ResponseUtil.wrapOrNotFound(extractProductVM);
    }

    @GetMapping("/products/extract-by-supplier/{supplierCode}")
    public ResponseEntity<List<ExtractProductVM>> getProductExtracts(@PathVariable String supplierCode) {
        log.debug("REST request to get Products : {}", supplierCode);
        ProductCriteria criteria = new ProductCriteria();
        Filter<String> stringFilter = new StringFilter().setEquals(supplierCode);
        StringFilter filterStr = (StringFilter) stringFilter;

        criteria.setSupplierCode(filterStr);
        Page<ProductDTO> page = productQueryService.findByCriteria(criteria, Pageable.unpaged());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/products/extract-by-supplier");
        return ResponseEntity.ok().headers(headers).body(createFromDtos((page.getContent())));
    }

    @PutMapping("/products/import-product")
    public ResponseEntity<List<ProductDTO>> updateProduct(@Valid @RequestBody List<ImportProductVM> importProductVMs) throws URISyntaxException {
        log.debug("REST request to import Product : {}", importProductVMs);
        if (importProductVMs.size() <= 0) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        List<ProductDTO> productDTOs =  productService.importProduct(importProductVMs);
        
        if(productDTOs.isEmpty()){
            throw new BadRequestAlertException("Invalid request", ENTITY_NAME, "idnull");
        }
        return ResponseEntity.ok()
            .body(productDTOs);
    }

    private Optional<ExtractProductVM> createOptionalExtractProduct(Optional<ProductDTO> productDTO) {
        return Optional.of(createVMFromDTO(productDTO.get()));
    }

    private ExtractProductVM createVMFromDTO(ProductDTO dto) {
        ExtractProductVM vm = new ExtractProductVM();
        vm.setBarcode(dto.getBarcode());
        vm.setProductName(dto.getName());
        vm.setSalePrice(dto.getSellingPrice());
        vm.setUnit(dto.getUnitName());
        vm.setUnitPrice(dto.getUnitPrice());
        vm.setSupplierAddress(dto.getSupplierAddress());
        vm.setSupplierCode(dto.getSupplierCode());
        vm.setSupplierNoTelp(dto.getSupplierNoTelp());
        vm.setSupplierName(dto.getSupplierName());
        vm.setWarehousePrice(dto.getWarehousePrice());
        return vm;
    }

    private List<ExtractProductVM> createFromDtos(List<ProductDTO> dtos) {
        List<ExtractProductVM> vms = new ArrayList<>();
        for (ProductDTO dto : dtos) {
            vms.add(createVMFromDTO(dto));
        }
        return vms;

    }

}
