package com.toko.maju.service.impl;

import com.toko.maju.service.CancelTransactionService;
import com.toko.maju.domain.CancelTransaction;
import com.toko.maju.repository.CancelTransactionRepository;
import com.toko.maju.repository.search.CancelTransactionSearchRepository;
import com.toko.maju.service.dto.CancelTransactionDTO;
import com.toko.maju.service.mapper.CancelTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CancelTransaction.
 */
@Service
@Transactional
public class CancelTransactionServiceImpl implements CancelTransactionService {

    private final Logger log = LoggerFactory.getLogger(CancelTransactionServiceImpl.class);

    private final CancelTransactionRepository cancelTransactionRepository;

    private final CancelTransactionMapper cancelTransactionMapper;

    private final CancelTransactionSearchRepository cancelTransactionSearchRepository;

    public CancelTransactionServiceImpl(CancelTransactionRepository cancelTransactionRepository, CancelTransactionMapper cancelTransactionMapper, CancelTransactionSearchRepository cancelTransactionSearchRepository) {
        this.cancelTransactionRepository = cancelTransactionRepository;
        this.cancelTransactionMapper = cancelTransactionMapper;
        this.cancelTransactionSearchRepository = cancelTransactionSearchRepository;
    }

    /**
     * Save a cancelTransaction.
     *
     * @param cancelTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CancelTransactionDTO save(CancelTransactionDTO cancelTransactionDTO) {
        log.debug("Request to save CancelTransaction : {}", cancelTransactionDTO);
        CancelTransaction cancelTransaction = cancelTransactionMapper.toEntity(cancelTransactionDTO);
        cancelTransaction = cancelTransactionRepository.save(cancelTransaction);
        CancelTransactionDTO result = cancelTransactionMapper.toDto(cancelTransaction);
        cancelTransactionSearchRepository.save(cancelTransaction);
        return result;
    }

    /**
     * Get all the cancelTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CancelTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CancelTransactions");
        return cancelTransactionRepository.findAll(pageable)
            .map(cancelTransactionMapper::toDto);
    }


    /**
     * Get one cancelTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CancelTransactionDTO> findOne(Long id) {
        log.debug("Request to get CancelTransaction : {}", id);
        return cancelTransactionRepository.findById(id)
            .map(cancelTransactionMapper::toDto);
    }

    /**
     * Delete the cancelTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CancelTransaction : {}", id);
        cancelTransactionRepository.deleteById(id);
        cancelTransactionSearchRepository.deleteById(id);
    }

    /**
     * Search for the cancelTransaction corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CancelTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CancelTransactions for query {}", query);
        return cancelTransactionSearchRepository.search(queryStringQuery(query), pageable)
            .map(cancelTransactionMapper::toDto);
    }
}
