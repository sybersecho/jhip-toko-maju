package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.SaleItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleItem and its DTO SaleItemDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, SaleTransactionsMapper.class})
public interface SaleItemMapper extends EntityMapper<SaleItemDTO, SaleItem> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "sale.id", target = "saleId")
    @Mapping(source = "sale.noInvoice", target = "saleNoInvoice")
    SaleItemDTO toDto(SaleItem saleItem);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "saleId", target = "sale")
    SaleItem toEntity(SaleItemDTO saleItemDTO);

    default SaleItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        SaleItem saleItem = new SaleItem();
        saleItem.setId(id);
        return saleItem;
    }
}
