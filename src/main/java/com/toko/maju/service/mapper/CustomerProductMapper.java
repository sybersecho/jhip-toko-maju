package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.CustomerProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerProduct and its DTO CustomerProductDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface CustomerProductMapper extends EntityMapper<CustomerProductDTO, CustomerProduct> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstName", target = "customerFirstName")
    CustomerProductDTO toDto(CustomerProduct customerProduct);

    @Mapping(source = "customerId", target = "customer")
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
