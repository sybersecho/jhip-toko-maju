package com.toko.maju.repository;

import com.toko.maju.domain.StockOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockOrderRepository extends JpaRepository<StockOrder, Long>, JpaSpecificationExecutor<StockOrder> {

    @Query("select stock_order from StockOrder stock_order where stock_order.creator.login = ?#{principal.username}")
    List<StockOrder> findByCreatorIsCurrentUser();

    @Query("select stock_order from StockOrder stock_order where stock_order.approval.login = ?#{principal.username}")
    List<StockOrder> findByApprovalIsCurrentUser();

}
