package com.toko.maju.repository;

import com.toko.maju.domain.BadStockProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the BadStockProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BadStockProductRepository extends JpaRepository<BadStockProduct, Long>, JpaSpecificationExecutor<BadStockProduct> {

    List<BadStockProduct> findByBarcodeIn(Iterable<String> barcodes);

    BadStockProduct findByBarcode(String barcode);
}
