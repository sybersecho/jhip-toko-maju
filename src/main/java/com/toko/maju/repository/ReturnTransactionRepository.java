package com.toko.maju.repository;

import com.toko.maju.domain.ReturnTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ReturnTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnTransactionRepository extends JpaRepository<ReturnTransaction, Long>, JpaSpecificationExecutor<ReturnTransaction> {

    @Query("select return_transaction from ReturnTransaction return_transaction where return_transaction.creator.login = ?#{principal.username}")
    List<ReturnTransaction> findByCreatorIsCurrentUser();

}
