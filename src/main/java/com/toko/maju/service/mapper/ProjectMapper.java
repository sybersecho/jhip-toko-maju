package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, UserMapper.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstName", target = "customerFirstName")
    @Mapping(source = "customer.lastName", target = "customerLastName")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    @Mapping(source = "changer.id", target = "changerId")
    @Mapping(source = "changer.login", target = "changerLogin")
    ProjectDTO toDto(Project project);

    @Mapping(target = "products", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "changerId", target = "changer")
    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }

    @AfterMapping
    default void setCustomerFullName(@MappingTarget ProjectDTO projectDTO) {
        projectDTO.setCustomerFullName(projectDTO.getCustomerFirstName() + " " + projectDTO.getCustomerLastName());

    }
}
