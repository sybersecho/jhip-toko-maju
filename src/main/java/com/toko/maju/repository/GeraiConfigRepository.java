package com.toko.maju.repository;

import com.toko.maju.domain.GeraiConfig;
import com.toko.maju.service.dto.GeraiConfigDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GeraiConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeraiConfigRepository extends JpaRepository<GeraiConfig, Long>, JpaSpecificationExecutor<GeraiConfig> {

}
