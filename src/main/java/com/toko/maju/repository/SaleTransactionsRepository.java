package com.toko.maju.repository;

import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.domain.enumeration.StatusTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data repository for the SaleTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleTransactionsRepository
    extends JpaRepository<SaleTransactions, Long>, JpaSpecificationExecutor<SaleTransactions> {

    @Query("select sale_transactions from SaleTransactions sale_transactions where sale_transactions.creator.login = ?#{principal.username}")
    List<SaleTransactions> findByCreatorIsCurrentUser();

    @Modifying
    @Query("update SaleTransactions s set s.remainingPayment = :remainingPayment, s.paid= :paid, s.settled = :settled, s.statusTransaction = :statusTransaction where s.id= :id")
    int updateDuePayment(@Param("remainingPayment") BigDecimal remainingPayment, @Param("paid") BigDecimal paid,
                         @Param("settled") Boolean settled, @Param("statusTransaction") StatusTransaction status, @Param("id") Long id);

}
