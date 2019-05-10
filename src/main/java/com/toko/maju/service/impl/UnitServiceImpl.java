package com.toko.maju.service.impl;

import com.toko.maju.service.UnitService;
import com.toko.maju.domain.Unit;
import com.toko.maju.repository.UnitRepository;
import com.toko.maju.repository.search.UnitSearchRepository;
import com.toko.maju.service.dto.UnitDTO;
import com.toko.maju.service.mapper.UnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Unit}.
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    private final UnitSearchRepository unitSearchRepository;

    public UnitServiceImpl(UnitRepository unitRepository, UnitMapper unitMapper, UnitSearchRepository unitSearchRepository) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
        this.unitSearchRepository = unitSearchRepository;
    }

    /**
     * Save a unit.
     *
     * @param unitDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UnitDTO save(UnitDTO unitDTO) {
        log.debug("Request to save Unit : {}", unitDTO);
        Unit unit = unitMapper.toEntity(unitDTO);
        unit = unitRepository.save(unit);
        UnitDTO result = unitMapper.toDto(unit);
        unitSearchRepository.save(unit);
        return result;
    }

    /**
     * Get all the units.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Units");
        return unitRepository.findAll(pageable)
            .map(unitMapper::toDto);
    }


    /**
     * Get one unit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UnitDTO> findOne(Long id) {
        log.debug("Request to get Unit : {}", id);
        return unitRepository.findById(id)
            .map(unitMapper::toDto);
    }

    /**
     * Delete the unit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Unit : {}", id);
        unitRepository.deleteById(id);
        unitSearchRepository.deleteById(id);
    }

    /**
     * Search for the unit corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Units for query {}", query);
        return unitSearchRepository.search(queryStringQuery(query), pageable)
            .map(unitMapper::toDto);
    }
}
