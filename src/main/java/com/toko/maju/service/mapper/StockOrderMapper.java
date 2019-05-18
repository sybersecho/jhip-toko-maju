package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.StockOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockOrder and its DTO StockOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StockOrderMapper extends EntityMapper<StockOrderDTO, StockOrder> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    @Mapping(source = "approval.id", target = "approvalId")
    @Mapping(source = "approval.login", target = "approvalLogin")
    StockOrderDTO toDto(StockOrder stockOrder);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "approvalId", target = "approval")
    StockOrder toEntity(StockOrderDTO stockOrderDTO);

    default StockOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOrder stockOrder = new StockOrder();
        stockOrder.setId(id);
        return stockOrder;
    }
}
