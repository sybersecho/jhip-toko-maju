package com.toko.maju.service.impl;

import com.toko.maju.service.GeraiService;
import com.toko.maju.domain.Gerai;
import com.toko.maju.repository.GeraiRepository;
import com.toko.maju.repository.search.GeraiSearchRepository;
import com.toko.maju.service.dto.GeraiDTO;
import com.toko.maju.service.mapper.GeraiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Gerai.
 */
@Service
@Transactional
public class GeraiServiceImpl implements GeraiService {

    private final Logger log = LoggerFactory.getLogger(GeraiServiceImpl.class);

    private final GeraiRepository geraiRepository;

    private final GeraiMapper geraiMapper;

    private final GeraiSearchRepository geraiSearchRepository;

    public GeraiServiceImpl(GeraiRepository geraiRepository, GeraiMapper geraiMapper, GeraiSearchRepository geraiSearchRepository) {
        this.geraiRepository = geraiRepository;
        this.geraiMapper = geraiMapper;
        this.geraiSearchRepository = geraiSearchRepository;
    }

    /**
     * Save a gerai.
     *
     * @param geraiDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GeraiDTO save(GeraiDTO geraiDTO) {
        log.debug("Request to save Gerai : {}", geraiDTO);
        Gerai gerai = geraiMapper.toEntity(geraiDTO);
        gerai = geraiRepository.save(gerai);
        GeraiDTO result = geraiMapper.toDto(gerai);
        geraiSearchRepository.save(gerai);
        return result;
    }

    /**
     * Get all the gerais.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeraiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Gerais");
        return geraiRepository.findAll(pageable)
            .map(geraiMapper::toDto);
    }


    /**
     * Get one gerai by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GeraiDTO> findOne(Long id) {
        log.debug("Request to get Gerai : {}", id);
        return geraiRepository.findById(id)
            .map(geraiMapper::toDto);
    }

    /**
     * Delete the gerai by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gerai : {}", id);
        geraiRepository.deleteById(id);
        geraiSearchRepository.deleteById(id);
    }

    /**
     * Search for the gerai corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeraiDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Gerais for query {}", query);
        return geraiSearchRepository.search(queryStringQuery(query), pageable)
            .map(geraiMapper::toDto);
    }
}
