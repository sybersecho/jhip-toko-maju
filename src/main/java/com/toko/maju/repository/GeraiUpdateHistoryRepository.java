package com.toko.maju.repository;

import com.toko.maju.domain.GeraiUpdateHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GeraiUpdateHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeraiUpdateHistoryRepository extends JpaRepository<GeraiUpdateHistory, Long>, JpaSpecificationExecutor<GeraiUpdateHistory> {

    GeraiUpdateHistory findTopByOrderByIdDesc();
}
