package com.toko.maju.repository;

import com.toko.maju.domain.StockOrderProcess;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockOrderProcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockOrderProcessRepository extends JpaRepository<StockOrderProcess, Long>, JpaSpecificationExecutor<StockOrderProcess> {

    @Query("select stock_order_process from StockOrderProcess stock_order_process where stock_order_process.creator.login = ?#{principal.username}")
    List<StockOrderProcess> findByCreatorIsCurrentUser();

}
