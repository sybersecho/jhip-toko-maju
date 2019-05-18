package com.toko.maju.repository;

import com.toko.maju.domain.StockOrderRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StockOrderRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockOrderRequestRepository extends JpaRepository<StockOrderRequest, Long>, JpaSpecificationExecutor<StockOrderRequest> {

}
