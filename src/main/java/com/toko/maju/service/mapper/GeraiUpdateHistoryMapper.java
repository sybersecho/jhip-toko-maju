package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.GeraiUpdateHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GeraiUpdateHistory and its DTO GeraiUpdateHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeraiUpdateHistoryMapper extends EntityMapper<GeraiUpdateHistoryDTO, GeraiUpdateHistory> {



    default GeraiUpdateHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        GeraiUpdateHistory geraiUpdateHistory = new GeraiUpdateHistory();
        geraiUpdateHistory.setId(id);
        return geraiUpdateHistory;
    }
}
