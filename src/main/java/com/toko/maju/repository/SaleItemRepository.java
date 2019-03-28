package com.toko.maju.repository;

import com.toko.maju.domain.Product;
import com.toko.maju.domain.SaleItem;
import com.toko.maju.domain.SaleTransactions;

import java.util.Set;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the SaleItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long>, JpaSpecificationExecutor<SaleItem> {
	Set<SaleItem> findBySale(SaleTransactions sale);

	@Query(value = "SELECT item.product FROM SaleItem item WHERE item.sale.id= :saleId")
	Set<Product> findProductsBySale(@Param("saleId") Long saleId);
}
