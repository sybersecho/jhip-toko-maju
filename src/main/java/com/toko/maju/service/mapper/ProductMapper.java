package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = {SupplierMapper.class, UnitMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "unit.name", target = "unitName")
    @Mapping(source = "supplier.code", target = "supplierCode")
    @Mapping(source = "supplier.address", target = "supplierAddress")
    @Mapping(source = "supplier.noTelp", target = "supplierNoTelp")
    ProductDTO toDto(Product product);

    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "unitId", target = "unit")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
