package com.toko.maju.web.rest;
import com.toko.maju.service.CustomerProductService;
import com.toko.maju.service.CustomerService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.dto.CustomerProductDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CustomerProduct.
 */
@RestController
@RequestMapping("/api")
public class CustomerProductResource {

    private final Logger log = LoggerFactory.getLogger(CustomerProductResource.class);

    private static final String ENTITY_NAME = "customerProduct";

    private final CustomerProductService customerProductService;
    
    @Autowired
    private final CustomerService customerService=null;

    public CustomerProductResource(CustomerProductService customerProductService) {
        this.customerProductService = customerProductService;
    }

    /**
     * POST  /customer-products : Create a new customerProduct.
     *
     * @param customerProductDTO the customerProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerProductDTO, or with status 400 (Bad Request) if the customerProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-products")
    public ResponseEntity<CustomerProductDTO> createCustomerProduct(@Valid @RequestBody CustomerProductDTO customerProductDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerProduct : {}", customerProductDTO);
        if (customerProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerProductDTO result = customerProductService.save(customerProductDTO);
        return ResponseEntity.created(new URI("/api/customer-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /customer-products : Create a new customerProduct.
     *
     * @param customerProductDTOs the customerProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerProductDTO, or with status 400 (Bad Request) if the customerProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-products/products")
    public ResponseEntity<CustomerProductDTO> createCustomerProducts(@RequestBody List<CustomerProductDTO> customerProductDTOs) throws URISyntaxException {
        log.debug("REST request to save CustomerProducts");
        if (customerProductDTOs.size() < 0) {
            throw new BadRequestAlertException("A new customerProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerProductDTO result = customerProductService.saveCustomerProducts(customerProductDTOs);
        return ResponseEntity.created(new URI("/api/customer/" + result.getCustomerId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCustomerCode()))
            .body(result);
    }

    /**
     * PUT  /customer-products : Updates an existing customerProduct.
     *
     * @param customerProductDTO the customerProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerProductDTO,
     * or with status 400 (Bad Request) if the customerProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-products")
    public ResponseEntity<CustomerProductDTO> updateCustomerProduct(@Valid @RequestBody CustomerProductDTO customerProductDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerProduct : {}", customerProductDTO);
        if (customerProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerProductDTO result = customerProductService.save(customerProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-products : get all the customerProducts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customerProducts in body
     */
    @GetMapping("/customer-products")
    public List<CustomerProductDTO> getAllCustomerProducts() {
        log.debug("REST request to get all CustomerProducts");
        return customerProductService.findAll();
    }

    /**
     * GET  /customer-products/:id : get the "id" customerProduct.
     *
     * @param id the id of the customerProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-products/{id}")
    public ResponseEntity<CustomerProductDTO> getCustomerProduct(@PathVariable Long id) {
        log.debug("REST request to get CustomerProduct : {}", id);
        Optional<CustomerProductDTO> customerProductDTO = customerProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerProductDTO);
    }

    /**
     * DELETE  /customer-products/:id : delete the "id" customerProduct.
     *
     * @param id the id of the customerProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-products/{id}")
    public ResponseEntity<Void> deleteCustomerProduct(@PathVariable Long id) {
        log.debug("REST request to delete CustomerProduct : {}", id);
        customerProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-products?query=:query : search for the customerProduct corresponding
     * to the query.
     *
     * @param query the query of the customerProduct search
     * @return the result of the search
     */
    @GetMapping("/_search/customer-products")
    public List<CustomerProductDTO> searchCustomerProducts(@RequestParam String query) {
        log.debug("REST request to search CustomerProducts for query {}", query);
        return customerProductService.search(query);
    }
    
    /**
     * GET  /customer-products/customers : get the customerProducts by Customer.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customerProducts in body
     */
    @GetMapping("/customer-products/customers/{id}")
    public List<CustomerProductDTO> getAllCustomerProductsByCustomer(@PathVariable Long id) {
        log.debug("REST request to get  CustomerProducts by Customer: {}", id);
        Optional<CustomerDTO> customer = customerService.findOne(id);
        
        return customerProductService.findByCustomer(customer.get());
    }

}
