package com.toko.maju.service.impl;

import com.toko.maju.service.StockOrderService;
import com.toko.maju.domain.StockOrder;
import com.toko.maju.repository.StockOrderRepository;
import com.toko.maju.repository.search.StockOrderSearchRepository;
import com.toko.maju.service.dto.StockOrderDTO;
import com.toko.maju.service.mapper.StockOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StockOrder.
 */
@Service
@Transactional
public class StockOrderServiceImpl implements StockOrderService {

    private final Logger log = LoggerFactory.getLogger(StockOrderServiceImpl.class);

    private final StockOrderRepository stockOrderRepository;

    private final StockOrderMapper stockOrderMapper;

    private final StockOrderSearchRepository stockOrderSearchRepository;

    public StockOrderServiceImpl(StockOrderRepository stockOrderRepository, StockOrderMapper stockOrderMapper, StockOrderSearchRepository stockOrderSearchRepository) {
        this.stockOrderRepository = stockOrderRepository;
        this.stockOrderMapper = stockOrderMapper;
        this.stockOrderSearchRepository = stockOrderSearchRepository;
    }

    /**
     * Save a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOrderDTO save(StockOrderDTO stockOrderDTO) {
        log.debug("Request to save StockOrder : {}", stockOrderDTO);
        StockOrder stockOrder = stockOrderMapper.toEntity(stockOrderDTO);
        stockOrder = stockOrderRepository.save(stockOrder);
        StockOrderDTO result = stockOrderMapper.toDto(stockOrder);
        stockOrderSearchRepository.save(stockOrder);
        return result;
    }

    /**
     * Get all the stockOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOrders");
        return stockOrderRepository.findAll(pageable)
            .map(stockOrderMapper::toDto);
    }


    /**
     * Get one stockOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StockOrderDTO> findOne(Long id) {
        log.debug("Request to get StockOrder : {}", id);
        return stockOrderRepository.findById(id)
            .map(stockOrderMapper::toDto);
    }

    /**
     * Delete the stockOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOrder : {}", id);
        stockOrderRepository.deleteById(id);
        stockOrderSearchRepository.deleteById(id);
    }

    /**
     * Search for the stockOrder corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StockOrders for query {}", query);
        return stockOrderSearchRepository.search(queryStringQuery(query), pageable)
            .map(stockOrderMapper::toDto);
    }
}
