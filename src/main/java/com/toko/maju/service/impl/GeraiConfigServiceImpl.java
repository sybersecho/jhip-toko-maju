package com.toko.maju.service.impl;

import com.toko.maju.service.GeraiConfigService;
import com.toko.maju.domain.GeraiConfig;
import com.toko.maju.repository.GeraiConfigRepository;
import com.toko.maju.repository.search.GeraiConfigSearchRepository;
import com.toko.maju.service.dto.GeraiConfigDTO;
import com.toko.maju.service.mapper.GeraiConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GeraiConfig.
 */
@Service
@Transactional
public class GeraiConfigServiceImpl implements GeraiConfigService {

    private final Logger log = LoggerFactory.getLogger(GeraiConfigServiceImpl.class);

    private final GeraiConfigRepository geraiConfigRepository;

    private final GeraiConfigMapper geraiConfigMapper;

    private final GeraiConfigSearchRepository geraiConfigSearchRepository;

    public GeraiConfigServiceImpl(GeraiConfigRepository geraiConfigRepository, GeraiConfigMapper geraiConfigMapper, GeraiConfigSearchRepository geraiConfigSearchRepository) {
        this.geraiConfigRepository = geraiConfigRepository;
        this.geraiConfigMapper = geraiConfigMapper;
        this.geraiConfigSearchRepository = geraiConfigSearchRepository;
    }

    /**
     * Save a geraiConfig.
     *
     * @param geraiConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GeraiConfigDTO save(GeraiConfigDTO geraiConfigDTO) {
        log.debug("Request to save GeraiConfig : {}", geraiConfigDTO);
        GeraiConfig geraiConfig = geraiConfigMapper.toEntity(geraiConfigDTO);
        geraiConfig = geraiConfigRepository.save(geraiConfig);
        GeraiConfigDTO result = geraiConfigMapper.toDto(geraiConfig);
        geraiConfigSearchRepository.save(geraiConfig);
        return result;
    }

    /**
     * Get all the geraiConfigs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GeraiConfigDTO> findAll() {
        log.debug("Request to get all GeraiConfigs");
        return geraiConfigRepository.findAll().stream()
            .map(geraiConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one geraiConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GeraiConfigDTO> findOne(Long id) {
        log.debug("Request to get GeraiConfig : {}", id);
        return geraiConfigRepository.findById(id)
            .map(geraiConfigMapper::toDto);
    }

    /**
     * Delete the geraiConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GeraiConfig : {}", id);
        geraiConfigRepository.deleteById(id);
        geraiConfigSearchRepository.deleteById(id);
    }

    /**
     * Search for the geraiConfig corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GeraiConfigDTO> search(String query) {
        log.debug("Request to search GeraiConfigs for query {}", query);
        return StreamSupport
            .stream(geraiConfigSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(geraiConfigMapper::toDto)
            .collect(Collectors.toList());
    }
}
