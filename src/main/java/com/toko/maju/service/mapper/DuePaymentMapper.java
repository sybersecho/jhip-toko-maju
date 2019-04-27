package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.DuePaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DuePayment and its DTO DuePaymentDTO.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SaleTransactionsMapper.class, CustomerMapper.class})
public interface DuePaymentMapper extends EntityMapper<DuePaymentDTO, DuePayment> {

	@Mapping(source = "creator.id", target = "creatorId")
	@Mapping(source = "creator.login", target = "creatorLogin")
	@Mapping(source = "sale.id", target = "saleId")
	@Mapping(source = "sale.noInvoice", target = "saleNoInvoice")
	@Mapping(source = "sale.customer.firstName", target = "customerFirstName")
	@Mapping(source = "sale.customer.lastName", target = "customerLastName")
	DuePaymentDTO toDto(DuePayment duePayment);

	@Mapping(source = "creatorId", target = "creator")
	@Mapping(source = "saleId", target = "sale")
	@Mapping(source = "saleId", target = "sale.id")
	@Mapping(source = "totalPayment", target = "sale.totalPayment")
	@Mapping(source = "paid", target = "sale.paid")
	@Mapping(source = "settled", target = "sale.settled")
	@Mapping(source = "customerId", target = "sale.customer")
	@Mapping(source = "saleNoInvoice", target = "sale.noInvoice")
	@Mapping(source = "remainingPayment", target = "sale.remainingPayment")
	DuePayment toEntity(DuePaymentDTO duePaymentDTO);

	default DuePayment fromId(Long id) {
		if (id == null) {
			return null;
		}
		DuePayment duePayment = new DuePayment();
		duePayment.setId(id);
		return duePayment;
	}
}
