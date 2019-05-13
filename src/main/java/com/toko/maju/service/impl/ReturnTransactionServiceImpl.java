package com.toko.maju.service.impl;

import com.toko.maju.service.ReturnTransactionService;
import com.toko.maju.domain.ReturnTransaction;
import com.toko.maju.repository.ReturnTransactionRepository;
import com.toko.maju.repository.search.ReturnTransactionSearchRepository;
import com.toko.maju.service.dto.ReturnTransactionDTO;
import com.toko.maju.service.mapper.ReturnTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReturnTransaction.
 */
@Service
@Transactional
public class ReturnTransactionServiceImpl implements ReturnTransactionService {

    private final Logger log = LoggerFactory.getLogger(ReturnTransactionServiceImpl.class);

    private final ReturnTransactionRepository returnTransactionRepository;

    private final ReturnTransactionMapper returnTransactionMapper;

    private final ReturnTransactionSearchRepository returnTransactionSearchRepository;

    public ReturnTransactionServiceImpl(ReturnTransactionRepository returnTransactionRepository, ReturnTransactionMapper returnTransactionMapper, ReturnTransactionSearchRepository returnTransactionSearchRepository) {
        this.returnTransactionRepository = returnTransactionRepository;
        this.returnTransactionMapper = returnTransactionMapper;
        this.returnTransactionSearchRepository = returnTransactionSearchRepository;
    }

    /**
     * Save a returnTransaction.
     *
     * @param returnTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReturnTransactionDTO save(ReturnTransactionDTO returnTransactionDTO) {
        log.debug("Request to save ReturnTransaction : {}", returnTransactionDTO);
        ReturnTransaction returnTransaction = returnTransactionMapper.toEntity(returnTransactionDTO);
        returnTransaction = returnTransactionRepository.save(returnTransaction);
        ReturnTransactionDTO result = returnTransactionMapper.toDto(returnTransaction);
        returnTransactionSearchRepository.save(returnTransaction);
        return result;
    }

    /**
     * Get all the returnTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReturnTransactions");
        return returnTransactionRepository.findAll(pageable)
            .map(returnTransactionMapper::toDto);
    }


    /**
     * Get one returnTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReturnTransactionDTO> findOne(Long id) {
        log.debug("Request to get ReturnTransaction : {}", id);
        return returnTransactionRepository.findById(id)
            .map(returnTransactionMapper::toDto);
    }

    /**
     * Delete the returnTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReturnTransaction : {}", id);
        returnTransactionRepository.deleteById(id);
        returnTransactionSearchRepository.deleteById(id);
    }

    /**
     * Search for the returnTransaction corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReturnTransactions for query {}", query);
        return returnTransactionSearchRepository.search(queryStringQuery(query), pageable)
            .map(returnTransactionMapper::toDto);
    }
}
