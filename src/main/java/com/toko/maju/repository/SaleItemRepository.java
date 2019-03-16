package com.toko.maju.repository;

import com.toko.maju.domain.SaleItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SaleItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long>, JpaSpecificationExecutor<SaleItem> {

}
