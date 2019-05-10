package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.CancelTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CancelTransaction and its DTO CancelTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {SaleTransactionsMapper.class})
public interface CancelTransactionMapper extends EntityMapper<CancelTransactionDTO, CancelTransaction> {

    @Mapping(source = "saleTransactions.id", target = "saleTransactionsId")
    @Mapping(source = "saleTransactions.noInvoice", target = "saleTransactionsNoInvoice")
    CancelTransactionDTO toDto(CancelTransaction cancelTransaction);

    @Mapping(source = "saleTransactionsId", target = "saleTransactions")
    CancelTransaction toEntity(CancelTransactionDTO cancelTransactionDTO);

    default CancelTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        CancelTransaction cancelTransaction = new CancelTransaction();
        cancelTransaction.setId(id);
        return cancelTransaction;
    }
}
