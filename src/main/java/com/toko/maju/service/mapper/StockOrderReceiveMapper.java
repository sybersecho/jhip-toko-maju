package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.StockOrderReceiveDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockOrderReceive and its DTO StockOrderReceiveDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StockOrderReceiveMapper extends EntityMapper<StockOrderReceiveDTO, StockOrderReceive> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    StockOrderReceiveDTO toDto(StockOrderReceive stockOrderReceive);

    @Mapping(source = "creatorId", target = "creator")
    StockOrderReceive toEntity(StockOrderReceiveDTO stockOrderReceiveDTO);

    default StockOrderReceive fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOrderReceive stockOrderReceive = new StockOrderReceive();
        stockOrderReceive.setId(id);
        return stockOrderReceive;
    }
}
