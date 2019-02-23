package com.toko.maju.repository;

import com.toko.maju.domain.Customer;
import com.toko.maju.domain.CustomerProduct;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the CustomerProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long> {
	List<CustomerProduct> findByCustomer(Customer customer);
}
