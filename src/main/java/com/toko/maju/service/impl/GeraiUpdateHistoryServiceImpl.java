package com.toko.maju.service.impl;

import com.toko.maju.service.GeraiUpdateHistoryService;
import com.toko.maju.domain.GeraiUpdateHistory;
import com.toko.maju.repository.GeraiUpdateHistoryRepository;
import com.toko.maju.repository.search.GeraiUpdateHistorySearchRepository;
import com.toko.maju.service.dto.GeraiUpdateHistoryDTO;
import com.toko.maju.service.mapper.GeraiUpdateHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GeraiUpdateHistory.
 */
@Service
@Transactional
public class GeraiUpdateHistoryServiceImpl implements GeraiUpdateHistoryService {

    private final Logger log = LoggerFactory.getLogger(GeraiUpdateHistoryServiceImpl.class);

    private final GeraiUpdateHistoryRepository geraiUpdateHistoryRepository;

    private final GeraiUpdateHistoryMapper geraiUpdateHistoryMapper;

    private final GeraiUpdateHistorySearchRepository geraiUpdateHistorySearchRepository;

    public GeraiUpdateHistoryServiceImpl(GeraiUpdateHistoryRepository geraiUpdateHistoryRepository, GeraiUpdateHistoryMapper geraiUpdateHistoryMapper, GeraiUpdateHistorySearchRepository geraiUpdateHistorySearchRepository) {
        this.geraiUpdateHistoryRepository = geraiUpdateHistoryRepository;
        this.geraiUpdateHistoryMapper = geraiUpdateHistoryMapper;
        this.geraiUpdateHistorySearchRepository = geraiUpdateHistorySearchRepository;
    }

    /**
     * Save a geraiUpdateHistory.
     *
     * @param geraiUpdateHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GeraiUpdateHistoryDTO save(GeraiUpdateHistoryDTO geraiUpdateHistoryDTO) {
        log.debug("Request to save GeraiUpdateHistory : {}", geraiUpdateHistoryDTO);
        GeraiUpdateHistory geraiUpdateHistory = geraiUpdateHistoryMapper.toEntity(geraiUpdateHistoryDTO);
        geraiUpdateHistory = geraiUpdateHistoryRepository.save(geraiUpdateHistory);
        GeraiUpdateHistoryDTO result = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);
        geraiUpdateHistorySearchRepository.save(geraiUpdateHistory);
        return result;
    }

    /**
     * Get all the geraiUpdateHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeraiUpdateHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeraiUpdateHistories");
        return geraiUpdateHistoryRepository.findAll(pageable)
            .map(geraiUpdateHistoryMapper::toDto);
    }


    /**
     * Get one geraiUpdateHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GeraiUpdateHistoryDTO> findOne(Long id) {
        log.debug("Request to get GeraiUpdateHistory : {}", id);
        return geraiUpdateHistoryRepository.findById(id)
            .map(geraiUpdateHistoryMapper::toDto);
    }

    /**
     * Delete the geraiUpdateHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GeraiUpdateHistory : {}", id);
        geraiUpdateHistoryRepository.deleteById(id);
        geraiUpdateHistorySearchRepository.deleteById(id);
    }

    /**
     * Search for the geraiUpdateHistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeraiUpdateHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GeraiUpdateHistories for query {}", query);
        return geraiUpdateHistorySearchRepository.search(queryStringQuery(query), pageable)
            .map(geraiUpdateHistoryMapper::toDto);
    }

    @Override
    public GeraiUpdateHistoryDTO findLatest() {
        return geraiUpdateHistoryMapper.toDto(geraiUpdateHistoryRepository.findTopByOrderByIdDesc());
    }
}
