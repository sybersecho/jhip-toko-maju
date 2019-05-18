package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.StockOrderProcessDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockOrderProcess and its DTO StockOrderProcessDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StockOrderProcessMapper extends EntityMapper<StockOrderProcessDTO, StockOrderProcess> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    StockOrderProcessDTO toDto(StockOrderProcess stockOrderProcess);

    @Mapping(source = "creatorId", target = "creator")
    StockOrderProcess toEntity(StockOrderProcessDTO stockOrderProcessDTO);

    default StockOrderProcess fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOrderProcess stockOrderProcess = new StockOrderProcess();
        stockOrderProcess.setId(id);
        return stockOrderProcess;
    }
}
