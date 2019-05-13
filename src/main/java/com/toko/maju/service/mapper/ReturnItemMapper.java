package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.ReturnItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReturnItem and its DTO ReturnItemDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, ReturnTransactionMapper.class})
public interface ReturnItemMapper extends EntityMapper<ReturnItemDTO, ReturnItem> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.barcode", target = "productBarcode")
    @Mapping(source = "returnTransaction.id", target = "returnTransactionId")
    ReturnItemDTO toDto(ReturnItem returnItem);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "returnTransactionId", target = "returnTransaction")
    ReturnItem toEntity(ReturnItemDTO returnItemDTO);

    default ReturnItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReturnItem returnItem = new ReturnItem();
        returnItem.setId(id);
        return returnItem;
    }
}
