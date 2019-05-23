package com.toko.maju.repository;

import com.toko.maju.domain.GeraiTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GeraiTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeraiTransactionRepository extends JpaRepository<GeraiTransaction, Long>, JpaSpecificationExecutor<GeraiTransaction> {

}
