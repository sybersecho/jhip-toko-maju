package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.SaleItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleItem and its DTO SaleItemDTO.
 */
@Mapper(componentModel = "spring", uses = {SaleTransactionsMapper.class, ProductMapper.class})
public interface SaleItemMapper extends EntityMapper<SaleItemDTO, SaleItem> {

    @Mapping(source = "sale.id", target = "saleId")
    @Mapping(source = "sale.noInvoice", target = "saleNoInvoice")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.barcode", target = "barcode")
    @Mapping(source = "product.sellingPrice", target = "sellingPrice")
    @Mapping(source = "product.unit", target = "unit")
    SaleItemDTO toDto(SaleItem saleItem);

    @Mapping(source = "saleId", target = "sale")
    @Mapping(source = "productId", target = "product")
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
