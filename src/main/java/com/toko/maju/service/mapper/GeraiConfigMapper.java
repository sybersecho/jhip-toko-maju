package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.GeraiConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GeraiConfig and its DTO GeraiConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeraiConfigMapper extends EntityMapper<GeraiConfigDTO, GeraiConfig> {



    default GeraiConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        GeraiConfig geraiConfig = new GeraiConfig();
        geraiConfig.setId(id);
        return geraiConfig;
    }
}
