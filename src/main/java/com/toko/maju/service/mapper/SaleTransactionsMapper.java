package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.SaleTransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleTransactions and its DTO SaleTransactionsDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, UserMapper.class, SaleItemMapper.class, DuePaymentMapper.class, ProjectMapper.class })
public interface SaleTransactionsMapper extends EntityMapper<SaleTransactionsDTO, SaleTransactions> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstName", target = "customerFirstName")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "customer.lastName", target = "customerLastName")
	@Mapping(source = "customer.code", target = "customerCode")
	@Mapping(source = "customer.address", target = "customerAddress")
	SaleTransactionsDTO toDto(SaleTransactions saleTransactions);

    @Mapping(target = "items", ignore = false)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "creatorId", target = "creator")
    @Mapping(target = "duePayments", ignore = true)
    @Mapping(source = "projectId", target = "project")
    SaleTransactions toEntity(SaleTransactionsDTO saleTransactionsDTO);

    default SaleTransactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        SaleTransactions saleTransactions = new SaleTransactions();
        saleTransactions.setId(id);
        return saleTransactions;
    }

    @AfterMapping
	default void linkItems(@MappingTarget SaleTransactions sale) {
		sale.getItems().stream().forEach(item -> item.setSale(sale));
	}
}
