package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.StockOrderRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockOrderRequest and its DTO StockOrderRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {StockOrderMapper.class})
public interface StockOrderRequestMapper extends EntityMapper<StockOrderRequestDTO, StockOrderRequest> {

    @Mapping(source = "stockOrder.id", target = "stockOrderId")
    StockOrderRequestDTO toDto(StockOrderRequest stockOrderRequest);

    @Mapping(source = "stockOrderId", target = "stockOrder")
    StockOrderRequest toEntity(StockOrderRequestDTO stockOrderRequestDTO);

    default StockOrderRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOrderRequest stockOrderRequest = new StockOrderRequest();
        stockOrderRequest.setId(id);
        return stockOrderRequest;
    }
}
