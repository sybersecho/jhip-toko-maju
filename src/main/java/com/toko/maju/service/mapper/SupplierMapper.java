package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.SupplierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Supplier and its DTO SupplierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {


    @Mapping(target = "products", ignore = true)
    Supplier toEntity(SupplierDTO supplierDTO);

    default Supplier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Supplier supplier = new Supplier();
        supplier.setId(id);
        return supplier;
    }
}
