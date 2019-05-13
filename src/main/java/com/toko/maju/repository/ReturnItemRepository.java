package com.toko.maju.repository;

import com.toko.maju.domain.ReturnItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReturnItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long>, JpaSpecificationExecutor<ReturnItem> {

}
