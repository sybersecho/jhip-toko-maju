package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.CustomerProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerProduct and its DTO CustomerProductDTO.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, ProductMapper.class })
public interface CustomerProductMapper extends EntityMapper<CustomerProductDTO, CustomerProduct> {

	@Mapping(source = "customer.id", target = "customerId")
	@Mapping(source = "customer.firstName", target = "customerFirstName")
	@Mapping(source = "customer.code", target = "customerCode")
	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "product.name", target = "productName")
	@Mapping(source = "product.unitPrice", target = "unitPrice")
    @Mapping(source = "product.sellingPrice", target = "sellingPrice")
    @Mapping(source = "product.barcode", target = "barcode")
    @Mapping(source = "product.unit", target = "unit")
	@Mapping(source = "product.supplier.code", target = "supplierCode")
	@Mapping(source = "product.supplier.name", target = "supplierName")
	CustomerProductDTO toDto(CustomerProduct customerProduct);

	@Mapping(source = "customerId", target = "customer")
	@Mapping(source = "productId", target = "product")
	CustomerProduct toEntity(CustomerProductDTO customerProductDTO);

	default CustomerProduct fromId(Long id) {
		if (id == null) {
			return null;
		}
		CustomerProduct customerProduct = new CustomerProduct();
		customerProduct.setId(id);
		return customerProduct;
	}
}
