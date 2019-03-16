package com.toko.maju.repository;

import com.toko.maju.domain.SaleTransactions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SaleTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleTransactionsRepository extends JpaRepository<SaleTransactions, Long>, JpaSpecificationExecutor<SaleTransactions> {

}
