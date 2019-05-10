package com.toko.maju.repository;

import com.toko.maju.domain.CancelTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CancelTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CancelTransactionRepository extends JpaRepository<CancelTransaction, Long>, JpaSpecificationExecutor<CancelTransaction> {

}
