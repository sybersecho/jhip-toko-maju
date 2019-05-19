package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.PurchaseListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PurchaseList and its DTO PurchaseListDTO.
 */
@Mapper(componentModel = "spring", uses = {PurchaseMapper.class})
public interface PurchaseListMapper extends EntityMapper<PurchaseListDTO, PurchaseList> {

    @Mapping(source = "purchase.id", target = "purchaseId")
    PurchaseListDTO toDto(PurchaseList purchaseList);

    @Mapping(source = "purchaseId", target = "purchase")
    PurchaseList toEntity(PurchaseListDTO purchaseListDTO);

    default PurchaseList fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseList purchaseList = new PurchaseList();
        purchaseList.setId(id);
        return purchaseList;
    }
}
