package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.SaleTransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleTransactions and its DTO SaleTransactionsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SaleTransactionsMapper extends EntityMapper<SaleTransactionsDTO, SaleTransactions> {


    @Mapping(target = "items", ignore = true)
    SaleTransactions toEntity(SaleTransactionsDTO saleTransactionsDTO);

    default SaleTransactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        SaleTransactions saleTransactions = new SaleTransactions();
        saleTransactions.setId(id);
        return saleTransactions;
    }
}
