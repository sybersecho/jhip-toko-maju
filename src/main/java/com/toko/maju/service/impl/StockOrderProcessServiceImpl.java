package com.toko.maju.service.impl;

import com.toko.maju.domain.User;
import com.toko.maju.repository.UserRepository;
import com.toko.maju.security.SecurityUtils;
import com.toko.maju.service.StockOrderProcessService;
import com.toko.maju.domain.StockOrderProcess;
import com.toko.maju.repository.StockOrderProcessRepository;
import com.toko.maju.repository.search.StockOrderProcessSearchRepository;
import com.toko.maju.service.dto.StockOrderProcessDTO;
import com.toko.maju.service.mapper.StockOrderProcessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StockOrderProcess.
 */
@Service
@Transactional
public class StockOrderProcessServiceImpl implements StockOrderProcessService {

    private final Logger log = LoggerFactory.getLogger(StockOrderProcessServiceImpl.class);

    private final StockOrderProcessRepository stockOrderProcessRepository;

    private final StockOrderProcessMapper stockOrderProcessMapper;

    private final StockOrderProcessSearchRepository stockOrderProcessSearchRepository;

    @Autowired
    private final UserRepository userRepository = null;

    public StockOrderProcessServiceImpl(StockOrderProcessRepository stockOrderProcessRepository, StockOrderProcessMapper stockOrderProcessMapper, StockOrderProcessSearchRepository stockOrderProcessSearchRepository) {
        this.stockOrderProcessRepository = stockOrderProcessRepository;
        this.stockOrderProcessMapper = stockOrderProcessMapper;
        this.stockOrderProcessSearchRepository = stockOrderProcessSearchRepository;
    }

    /**
     * Save a stockOrderProcess.
     *
     * @param stockOrderProcessDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOrderProcessDTO save(StockOrderProcessDTO stockOrderProcessDTO) {
        log.debug("Request to save StockOrderProcess : {}", stockOrderProcessDTO);
        StockOrderProcess stockOrderProcess = stockOrderProcessMapper.toEntity(stockOrderProcessDTO);
        stockOrderProcess = stockOrderProcessRepository.save(stockOrderProcess);
        StockOrderProcessDTO result = stockOrderProcessMapper.toDto(stockOrderProcess);
        stockOrderProcessSearchRepository.save(stockOrderProcess);
        return result;
    }

    /**
     * Get all the stockOrderProcesses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderProcessDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOrderProcesses");
        return stockOrderProcessRepository.findAll(pageable)
            .map(stockOrderProcessMapper::toDto);
    }


    /**
     * Get one stockOrderProcess by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StockOrderProcessDTO> findOne(Long id) {
        log.debug("Request to get StockOrderProcess : {}", id);
        return stockOrderProcessRepository.findById(id)
            .map(stockOrderProcessMapper::toDto);
    }

    /**
     * Delete the stockOrderProcess by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOrderProcess : {}", id);
        stockOrderProcessRepository.deleteById(id);
        stockOrderProcessSearchRepository.deleteById(id);
    }

    /**
     * Search for the stockOrderProcess corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderProcessDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StockOrderProcesses for query {}", query);
        return stockOrderProcessSearchRepository.search(queryStringQuery(query), pageable)
            .map(stockOrderProcessMapper::toDto);
    }

    @Override
    public List<StockOrderProcessDTO> saveAll(List<StockOrderProcessDTO> stockOrderProcessDTOS) {
        List<StockOrderProcess> stockOrderProcesses = stockOrderProcessMapper.toEntity(stockOrderProcessDTOS);
        stockOrderProcesses = stockOrderProcessRepository.saveAll(stockOrderProcesses);
        String login = SecurityUtils.getCurrentUserLogin().get();
        User u = userRepository.findOneByLogin(login).get();
        stockOrderProcesses.forEach(it -> it.setCreator(u));
        // TODO
        // get product by barcode
        // ubah qty
        // update
        stockOrderProcessSearchRepository.saveAll(stockOrderProcesses);
        return stockOrderProcessMapper.toDto(stockOrderProcesses);
    }
}
