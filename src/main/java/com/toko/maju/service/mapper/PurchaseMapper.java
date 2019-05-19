package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.PurchaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Purchase and its DTO PurchaseDTO.
 */
@Mapper(componentModel = "spring", uses = {SupplierMapper.class, UserMapper.class})
public interface PurchaseMapper extends EntityMapper<PurchaseDTO, Purchase> {

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    PurchaseDTO toDto(Purchase purchase);

    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "creatorId", target = "creator")
    @Mapping(target = "purchaseLists", ignore = false)
    Purchase toEntity(PurchaseDTO purchaseDTO);

    default Purchase fromId(Long id) {
        if (id == null) {
            return null;
        }
        Purchase purchase = new Purchase();
        purchase.setId(id);
        return purchase;
    }

    @AfterMapping
    default void linkItems(@MappingTarget Purchase purchase) {
        purchase.getPurchaseLists().stream().forEach(item -> item.setPurchase(purchase));
    }
}
