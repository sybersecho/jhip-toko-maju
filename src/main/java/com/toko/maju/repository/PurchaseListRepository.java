package com.toko.maju.repository;

import com.toko.maju.domain.PurchaseList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Long>, JpaSpecificationExecutor<PurchaseList> {

}
