package com.toko.maju.repository;

import com.toko.maju.domain.StockOrderReceive;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockOrderReceive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockOrderReceiveRepository extends JpaRepository<StockOrderReceive, Long>, JpaSpecificationExecutor<StockOrderReceive> {

    @Query("select stock_order_receive from StockOrderReceive stock_order_receive where stock_order_receive.creator.login = ?#{principal.username}")
    List<StockOrderReceive> findByCreatorIsCurrentUser();

}
