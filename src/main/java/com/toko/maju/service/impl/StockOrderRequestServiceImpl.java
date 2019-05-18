package com.toko.maju.service.impl;

import com.toko.maju.service.StockOrderRequestService;
import com.toko.maju.domain.StockOrderRequest;
import com.toko.maju.repository.StockOrderRequestRepository;
import com.toko.maju.repository.search.StockOrderRequestSearchRepository;
import com.toko.maju.service.dto.StockOrderRequestDTO;
import com.toko.maju.service.mapper.StockOrderRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StockOrderRequest.
 */
@Service
@Transactional
public class StockOrderRequestServiceImpl implements StockOrderRequestService {

    private final Logger log = LoggerFactory.getLogger(StockOrderRequestServiceImpl.class);

    private final StockOrderRequestRepository stockOrderRequestRepository;

    private final StockOrderRequestMapper stockOrderRequestMapper;

    private final StockOrderRequestSearchRepository stockOrderRequestSearchRepository;

    public StockOrderRequestServiceImpl(StockOrderRequestRepository stockOrderRequestRepository, StockOrderRequestMapper stockOrderRequestMapper, StockOrderRequestSearchRepository stockOrderRequestSearchRepository) {
        this.stockOrderRequestRepository = stockOrderRequestRepository;
        this.stockOrderRequestMapper = stockOrderRequestMapper;
        this.stockOrderRequestSearchRepository = stockOrderRequestSearchRepository;
    }

    /**
     * Save a stockOrderRequest.
     *
     * @param stockOrderRequestDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOrderRequestDTO save(StockOrderRequestDTO stockOrderRequestDTO) {
        log.debug("Request to save StockOrderRequest : {}", stockOrderRequestDTO);
        StockOrderRequest stockOrderRequest = stockOrderRequestMapper.toEntity(stockOrderRequestDTO);
        stockOrderRequest = stockOrderRequestRepository.save(stockOrderRequest);
        StockOrderRequestDTO result = stockOrderRequestMapper.toDto(stockOrderRequest);
        stockOrderRequestSearchRepository.save(stockOrderRequest);
        return result;
    }

    /**
     * Get all the stockOrderRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOrderRequests");
        return stockOrderRequestRepository.findAll(pageable)
            .map(stockOrderRequestMapper::toDto);
    }


    /**
     * Get one stockOrderRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StockOrderRequestDTO> findOne(Long id) {
        log.debug("Request to get StockOrderRequest : {}", id);
        return stockOrderRequestRepository.findById(id)
            .map(stockOrderRequestMapper::toDto);
    }

    /**
     * Delete the stockOrderRequest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOrderRequest : {}", id);
        stockOrderRequestRepository.deleteById(id);
        stockOrderRequestSearchRepository.deleteById(id);
    }

    /**
     * Search for the stockOrderRequest corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderRequestDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StockOrderRequests for query {}", query);
        return stockOrderRequestSearchRepository.search(queryStringQuery(query), pageable)
            .map(stockOrderRequestMapper::toDto);
    }

    @Override
    public List<StockOrderRequestDTO> saveAll(List<StockOrderRequestDTO> orderRequestDTOS) {
        log.debug("Request to save: {}", orderRequestDTOS);
        final List<StockOrderRequest> result = stockOrderRequestRepository.saveAll(stockOrderRequestMapper.toEntity(orderRequestDTOS));
        stockOrderRequestSearchRepository.saveAll(result);
        return stockOrderRequestMapper.toDto(result);
    }
}
