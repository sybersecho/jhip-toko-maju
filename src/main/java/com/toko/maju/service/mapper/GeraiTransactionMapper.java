package com.toko.maju.service.mapper;

import com.toko.maju.domain.*;
import com.toko.maju.service.dto.GeraiTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GeraiTransaction and its DTO GeraiTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {GeraiMapper.class})
public interface GeraiTransactionMapper extends EntityMapper<GeraiTransactionDTO, GeraiTransaction> {

    @Mapping(source = "gerai.id", target = "geraiId")
    @Mapping(source = "gerai.name", target = "geraiName")
    @Mapping(source = "gerai.code", target = "geraiCode")
    GeraiTransactionDTO toDto(GeraiTransaction geraiTransaction);

    @Mapping(source = "geraiId", target = "gerai")
    GeraiTransaction toEntity(GeraiTransactionDTO geraiTransactionDTO);

    default GeraiTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        GeraiTransaction geraiTransaction = new GeraiTransaction();
        geraiTransaction.setId(id);
        return geraiTransaction;
    }
}
