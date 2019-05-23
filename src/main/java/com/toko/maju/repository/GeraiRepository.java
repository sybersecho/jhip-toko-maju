package com.toko.maju.repository;

import com.toko.maju.domain.Gerai;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Gerai entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeraiRepository extends JpaRepository<Gerai, Long>, JpaSpecificationExecutor<Gerai> {

    @Query("select gerai from Gerai gerai where gerai.creator.login = ?#{principal.username}")
    List<Gerai> findByCreatorIsCurrentUser();

    Optional<Gerai> findByCode(String code);
}
