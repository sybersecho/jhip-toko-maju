package com.toko.maju.repository;

import com.toko.maju.domain.Purchase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Purchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {

    @Query("select purchase from Purchase purchase where purchase.creator.login = ?#{principal.username}")
    List<Purchase> findByCreatorIsCurrentUser();

}
