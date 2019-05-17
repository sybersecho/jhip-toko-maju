package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.BadStockProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BadStockProduct and its DTO BadStockProductDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BadStockProductMapper extends EntityMapper<BadStockProductDTO, BadStockProduct> {



    default BadStockProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        BadStockProduct badStockProduct = new BadStockProduct();
        badStockProduct.setId(id);
        return badStockProduct;
    }
}
