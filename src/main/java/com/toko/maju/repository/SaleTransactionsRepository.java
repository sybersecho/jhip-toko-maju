package com.toko.maju.repository;

import com.toko.maju.domain.SaleTransactions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SaleTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleTransactionsRepository extends JpaRepository<SaleTransactions, Long>, JpaSpecificationExecutor<SaleTransactions> {

    @Query("select sale_transactions from SaleTransactions sale_transactions where sale_transactions.creator.login = ?#{principal.username}")
    List<SaleTransactions> findByCreatorIsCurrentUser();

}
