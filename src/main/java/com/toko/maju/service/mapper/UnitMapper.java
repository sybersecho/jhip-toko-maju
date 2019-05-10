package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.UnitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Unit and its DTO UnitDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UnitMapper extends EntityMapper<UnitDTO, Unit> {



    default Unit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Unit unit = new Unit();
        unit.setId(id);
        return unit;
    }
}
