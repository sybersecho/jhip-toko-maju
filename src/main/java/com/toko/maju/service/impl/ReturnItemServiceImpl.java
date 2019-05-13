package com.toko.maju.service.impl;

import com.toko.maju.service.ReturnItemService;
import com.toko.maju.domain.ReturnItem;
import com.toko.maju.repository.ReturnItemRepository;
import com.toko.maju.repository.search.ReturnItemSearchRepository;
import com.toko.maju.service.dto.ReturnItemDTO;
import com.toko.maju.service.mapper.ReturnItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReturnItem.
 */
@Service
@Transactional
public class ReturnItemServiceImpl implements ReturnItemService {

    private final Logger log = LoggerFactory.getLogger(ReturnItemServiceImpl.class);

    private final ReturnItemRepository returnItemRepository;

    private final ReturnItemMapper returnItemMapper;

    private final ReturnItemSearchRepository returnItemSearchRepository;

    public ReturnItemServiceImpl(ReturnItemRepository returnItemRepository, ReturnItemMapper returnItemMapper, ReturnItemSearchRepository returnItemSearchRepository) {
        this.returnItemRepository = returnItemRepository;
        this.returnItemMapper = returnItemMapper;
        this.returnItemSearchRepository = returnItemSearchRepository;
    }

    /**
     * Save a returnItem.
     *
     * @param returnItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReturnItemDTO save(ReturnItemDTO returnItemDTO) {
        log.debug("Request to save ReturnItem : {}", returnItemDTO);
        ReturnItem returnItem = returnItemMapper.toEntity(returnItemDTO);
        returnItem = returnItemRepository.save(returnItem);
        ReturnItemDTO result = returnItemMapper.toDto(returnItem);
        returnItemSearchRepository.save(returnItem);
        return result;
    }

    /**
     * Get all the returnItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReturnItems");
        return returnItemRepository.findAll(pageable)
            .map(returnItemMapper::toDto);
    }


    /**
     * Get one returnItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReturnItemDTO> findOne(Long id) {
        log.debug("Request to get ReturnItem : {}", id);
        return returnItemRepository.findById(id)
            .map(returnItemMapper::toDto);
    }

    /**
     * Delete the returnItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReturnItem : {}", id);
        returnItemRepository.deleteById(id);
        returnItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the returnItem corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReturnItems for query {}", query);
        return returnItemSearchRepository.search(queryStringQuery(query), pageable)
            .map(returnItemMapper::toDto);
    }
}
