package com.toko.maju.service.impl;

import com.toko.maju.service.GeraiTransactionService;
import com.toko.maju.domain.GeraiTransaction;
import com.toko.maju.repository.GeraiTransactionRepository;
import com.toko.maju.repository.search.GeraiTransactionSearchRepository;
import com.toko.maju.service.dto.GeraiTransactionDTO;
import com.toko.maju.service.mapper.GeraiTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GeraiTransaction.
 */
@Service
@Transactional
public class GeraiTransactionServiceImpl implements GeraiTransactionService {

    private final Logger log = LoggerFactory.getLogger(GeraiTransactionServiceImpl.class);

    private final GeraiTransactionRepository geraiTransactionRepository;

    private final GeraiTransactionMapper geraiTransactionMapper;

    private final GeraiTransactionSearchRepository geraiTransactionSearchRepository;

    public GeraiTransactionServiceImpl(GeraiTransactionRepository geraiTransactionRepository, GeraiTransactionMapper geraiTransactionMapper, GeraiTransactionSearchRepository geraiTransactionSearchRepository) {
        this.geraiTransactionRepository = geraiTransactionRepository;
        this.geraiTransactionMapper = geraiTransactionMapper;
        this.geraiTransactionSearchRepository = geraiTransactionSearchRepository;
    }

    /**
     * Save a geraiTransaction.
     *
     * @param geraiTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GeraiTransactionDTO save(GeraiTransactionDTO geraiTransactionDTO) {
        log.debug("Request to save GeraiTransaction : {}", geraiTransactionDTO);
        GeraiTransaction geraiTransaction = geraiTransactionMapper.toEntity(geraiTransactionDTO);
        geraiTransaction = geraiTransactionRepository.save(geraiTransaction);
        GeraiTransactionDTO result = geraiTransactionMapper.toDto(geraiTransaction);
        geraiTransactionSearchRepository.save(geraiTransaction);
        return result;
    }

    /**
     * Get all the geraiTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeraiTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeraiTransactions");
        return geraiTransactionRepository.findAll(pageable)
            .map(geraiTransactionMapper::toDto);
    }


    /**
     * Get one geraiTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GeraiTransactionDTO> findOne(Long id) {
        log.debug("Request to get GeraiTransaction : {}", id);
        return geraiTransactionRepository.findById(id)
            .map(geraiTransactionMapper::toDto);
    }

    /**
     * Delete the geraiTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GeraiTransaction : {}", id);
        geraiTransactionRepository.deleteById(id);
        geraiTransactionSearchRepository.deleteById(id);
    }

    /**
     * Search for the geraiTransaction corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeraiTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GeraiTransactions for query {}", query);
        return geraiTransactionSearchRepository.search(queryStringQuery(query), pageable)
            .map(geraiTransactionMapper::toDto);
    }

    @Override
    public List<GeraiTransactionDTO> saveAll(List<GeraiTransactionDTO> geraiTransactionDTOS) {
        List<GeraiTransaction> geraiTransactions = geraiTransactionMapper.toEntity(geraiTransactionDTOS);
        geraiTransactions = geraiTransactionRepository.saveAll(geraiTransactions);
        geraiTransactionSearchRepository.saveAll(geraiTransactions);
        return geraiTransactionMapper.toDto(geraiTransactions);
    }
}
