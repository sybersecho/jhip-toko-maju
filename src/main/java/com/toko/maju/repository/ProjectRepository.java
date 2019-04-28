package com.toko.maju.repository;

import com.toko.maju.domain.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @Query("select project from Project project where project.creator.login = ?#{principal.username}")
    List<Project> findByCreatorIsCurrentUser();

    @Query("select project from Project project where project.changer.login = ?#{principal.username}")
    List<Project> findByChangerIsCurrentUser();

}
