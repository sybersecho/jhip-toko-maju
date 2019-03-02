package com.toko.maju.repository;

import com.toko.maju.domain.ProjectProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProjectProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectProductRepository extends JpaRepository<ProjectProduct, Long>, JpaSpecificationExecutor<ProjectProduct> {

}
