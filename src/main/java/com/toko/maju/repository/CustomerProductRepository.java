package com.toko.maju.repository;

import com.toko.maju.domain.CustomerProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CustomerProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long>, JpaSpecificationExecutor<CustomerProduct> {

}
