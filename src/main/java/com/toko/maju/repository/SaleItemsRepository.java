package com.toko.maju.repository;

import com.toko.maju.domain.SaleItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SaleItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleItemsRepository extends JpaRepository<SaleItems, Long> {

}
