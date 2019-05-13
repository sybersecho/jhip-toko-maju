package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.ReturnTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReturnTransaction and its DTO ReturnTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CustomerMapper.class, SupplierMapper.class})
public interface ReturnTransactionMapper extends EntityMapper<ReturnTransactionDTO, ReturnTransaction> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.code", target = "customerCode")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.code", target = "supplierCode")
    ReturnTransactionDTO toDto(ReturnTransaction returnTransaction);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(target = "returnItems", ignore = true)
    ReturnTransaction toEntity(ReturnTransactionDTO returnTransactionDTO);

    default ReturnTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReturnTransaction returnTransaction = new ReturnTransaction();
        returnTransaction.setId(id);
        return returnTransaction;
    }
}
