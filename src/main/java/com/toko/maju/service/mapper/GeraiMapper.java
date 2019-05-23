package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.GeraiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Gerai and its DTO GeraiDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface GeraiMapper extends EntityMapper<GeraiDTO, Gerai> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    GeraiDTO toDto(Gerai gerai);

    @Mapping(source = "creatorId", target = "creator")
    Gerai toEntity(GeraiDTO geraiDTO);

    default Gerai fromId(Long id) {
        if (id == null) {
            return null;
        }
        Gerai gerai = new Gerai();
        gerai.setId(id);
        return gerai;
    }
}
