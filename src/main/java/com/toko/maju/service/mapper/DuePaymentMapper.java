package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.DuePaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DuePayment and its DTO DuePaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DuePaymentMapper extends EntityMapper<DuePaymentDTO, DuePayment> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    DuePaymentDTO toDto(DuePayment duePayment);

    @Mapping(source = "creatorId", target = "creator")
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
