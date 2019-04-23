package com.toko.maju.repository;

import com.toko.maju.domain.DuePayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the DuePayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DuePaymentRepository extends JpaRepository<DuePayment, Long>, JpaSpecificationExecutor<DuePayment> {

    @Query("select due_payment from DuePayment due_payment where due_payment.creator.login = ?#{principal.username}")
    List<DuePayment> findByCreatorIsCurrentUser();

}
