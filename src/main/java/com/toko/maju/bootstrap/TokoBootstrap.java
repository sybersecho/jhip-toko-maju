package com.toko.maju.bootstrap;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.toko.maju.domain.Customer;
import com.toko.maju.domain.CustomerProduct;
import com.toko.maju.domain.Product;
import com.toko.maju.domain.enumeration.Gender;
import com.toko.maju.domain.enumeration.UnitMeasure;
import com.toko.maju.repository.CustomerProductRepository;
import com.toko.maju.repository.CustomerRepository;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.search.CustomerProductSearchRepository;
import com.toko.maju.repository.search.CustomerSearchRepository;
import com.toko.maju.repository.search.ProductSearchRepository;

/**
 * TokoBootstrap
 */
@Component
public class TokoBootstrap implements CommandLineRunner {

    private CustomerRepository customerRepo;
    private ProductRepository productRepo;
    private CustomerProductRepository customerProductRepo;
    
//    @Autowired
//    private CustomerMapper customerMapper;
//    @Autowired
//    private ProductMapper productMapper;
//    @Autowired
//    private CustomerProductMapper customerProductMapper;
    
    @Autowired
    private CustomerSearchRepository customSearchRepository;
    @Autowired
    private ProductSearchRepository productSearchRepository;
    @Autowired
    private CustomerProductSearchRepository customerProductSearchRepository;

    public TokoBootstrap(CustomerRepository customerRepo, ProductRepository productRepo,
            CustomerProductRepository customerProductRepo) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.customerProductRepo = customerProductRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCustomers();
        loadProducts();
        loadCustomerProduct();
    }

    private void loadCustomerProduct() {
        List<Product> products = productRepo.findAll();
        List<Customer> customers = customerRepo.findAll();
        Customer c1 = customers.get(0);
        CustomerProduct cProduct = new CustomerProduct();
        cProduct.setCustomer(c1);
        cProduct.setProduct(products.get(0));
        cProduct.setSpecialPrice(BigDecimal.valueOf(1000));

        cProduct= customerProductRepo.save(cProduct);
        customerProductSearchRepository.save(cProduct);

        CustomerProduct cProduct2 = new CustomerProduct();
        cProduct2.setCustomer(c1);
        cProduct2.setProduct(products.get(1));
        cProduct2.setSpecialPrice(BigDecimal.valueOf(1000));
        

        cProduct2 = customerProductRepo.save(cProduct2);
        customerProductSearchRepository.save(cProduct2);

    }

    private void loadProducts() {
        Product product1 = new Product();
        product1.setBarcode("02202020");
        product1.setName("Sampurna");
        product1.setSellingPrices(BigDecimal.valueOf(10000));
        product1.setStock(20);
        product1.setUnit(UnitMeasure.KG);
        product1.setUnitPrices(BigDecimal.valueOf(10000));
        product1.setWarehousePrices(BigDecimal.valueOf(10000));
        productRepo.save(product1);
        productSearchRepository.save(product1);

        Product product2 = new Product();
        product2.setBarcode("121202202020");
        product2.setName("Sampurna 2");
        product2.setSellingPrices(BigDecimal.valueOf(10000));
        product2.setStock(20);
        product2.setUnit(UnitMeasure.KG);
        product2.setUnitPrices(BigDecimal.valueOf(10000));
        product2.setWarehousePrices(BigDecimal.valueOf(10000));
        productRepo.save(product2);
        productSearchRepository.save(product2);
    }

    private void loadCustomers() {
        Customer customer1 = new Customer();
        customer1.setAddress("Address 1");
        customer1.setCode("Code 1");
        customer1.setFirstName("Customer");
        customer1.setLastName("Last name");
        customer1.setPhoneNumber("123 12312321");
        customer1.setGender(Gender.FEMALE);

        customer1 = customerRepo.save(customer1);
        customSearchRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setAddress("Address 2");
        customer2.setCode("Code 2");
        customer2.setFirstName("Customer");
        customer2.setLastName("Last name");
        customer2.setPhoneNumber("123 12312321");
        customer2.setGender(Gender.FEMALE);

        customer2 = customerRepo.save(customer2);
        customSearchRepository.save(customer2);
    }
}
