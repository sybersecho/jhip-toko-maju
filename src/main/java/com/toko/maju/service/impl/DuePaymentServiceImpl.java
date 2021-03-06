package com.toko.maju.service.impl;

import com.toko.maju.domain.enumeration.StatusTransaction;
import com.toko.maju.service.DuePaymentService;
import com.toko.maju.domain.DuePayment;
import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.repository.DuePaymentRepository;
import com.toko.maju.repository.SaleTransactionsRepository;
import com.toko.maju.repository.search.DuePaymentSearchRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.service.mapper.DuePaymentMapper;
import com.toko.maju.service.mapper.SaleTransactionsMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DuePayment.
 */
@Service
@Transactional
public class DuePaymentServiceImpl implements DuePaymentService {

    private final Logger log = LoggerFactory.getLogger(DuePaymentServiceImpl.class);

    private final DuePaymentRepository duePaymentRepository;

    private final DuePaymentMapper duePaymentMapper;

    @Autowired
    private final SaleTransactionsMapper saleTransactionsMapper = null;

    @Autowired
    private final SaleTransactionsSearchRepository SaleTransactionsSearchRepository = null;

    private final DuePaymentSearchRepository duePaymentSearchRepository;

    @Autowired
    private final SaleTransactionsRepository saleTransactionsRepository = null;

    public DuePaymentServiceImpl(DuePaymentRepository duePaymentRepository, DuePaymentMapper duePaymentMapper,
                                 DuePaymentSearchRepository duePaymentSearchRepository) {
        this.duePaymentRepository = duePaymentRepository;
        this.duePaymentMapper = duePaymentMapper;
        this.duePaymentSearchRepository = duePaymentSearchRepository;
    }

    /**
     * Save a duePayment.
     *
     * @param duePaymentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DuePaymentDTO save(DuePaymentDTO duePaymentDTO) {
        log.debug("Request to save DuePayment : {}", duePaymentDTO);
        DuePayment duePayment = duePaymentMapper.toEntity(duePaymentDTO);
        SaleTransactions sale = duePayment.getSale();
        sale.setCreator(duePayment.getCreator());
        sale.addDuePayment(duePayment);
        log.debug("Entity DuePayment : {}", duePayment);

        if (sale.getRemainingPayment().equals(BigDecimal.ZERO)) {
            sale.setStatusTransaction(StatusTransaction.PAID);
        }
        saleTransactionsRepository.updateDuePayment(sale.getRemainingPayment(), sale.getPaid(), sale.isSettled(), sale.getStatusTransaction(),
            sale.getId());
        duePayment = duePaymentRepository.save(duePayment);
        DuePaymentDTO result = duePaymentMapper.toDto(duePayment);
        duePaymentSearchRepository.save(duePayment);
        return result;
    }

    /**
     * Get all the duePayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DuePaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DuePayments");
        return duePaymentRepository.findAll(pageable).map(duePaymentMapper::toDto);
    }

    /**
     * Get one duePayment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DuePaymentDTO> findOne(Long id) {
        log.debug("Request to get DuePayment : {}", id);
        return duePaymentRepository.findById(id).map(duePaymentMapper::toDto);
    }

    /**
     * Delete the duePayment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DuePayment : {}", id);
        duePaymentRepository.deleteById(id);
        duePaymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the duePayment corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DuePaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DuePayments for query {}", query);
        return duePaymentSearchRepository.search(queryStringQuery(query), pageable).map(duePaymentMapper::toDto);
    }

    @Override
    @Transactional
    public List<DuePaymentDTO> saveDuePayment(List<DuePaymentDTO> duePaymentDTOs,
                                              List<SaleTransactionsDTO> saleTransactionsDTOs) {
        List<DuePayment> duePayments = duePaymentMapper.toEntity(duePaymentDTOs);
        log.debug("Due Entity, size: {}, string {}", duePayments.size(), duePayments);
        List<SaleTransactions> saleTransactions = saleTransactionsMapper.toEntity(saleTransactionsDTOs);
        log.debug("Sale Entity before, size: {}, string: {}", saleTransactions.size(), saleTransactions);
        for (SaleTransactions sale : saleTransactions) {
            log.debug("looking for sale: {}", sale);
            DuePayment due = duePayments.stream().filter(d -> d.getSale().getId().equals(sale.getId())).findFirst()
                .get();
            sale.setPaid(sale.getPaid().add(due.getPaid()));
            sale.setSettled(due.isSettled());
            sale.setRemainingPayment(due.getRemainingPayment());
            if (sale.getRemainingPayment().equals(BigDecimal.ZERO)) {
                sale.setStatusTransaction(StatusTransaction.PAID);
            }
            saleTransactionsRepository.updateDuePayment(sale.getRemainingPayment(), sale.getPaid(), sale.isSettled(), sale.getStatusTransaction(),
                sale.getId());
        }
        log.debug("Sale Entity {} after", saleTransactions);

        log.debug("Saving Due {}", duePayments);
        duePayments = duePaymentRepository.saveAll(duePayments);
        duePaymentSearchRepository.saveAll(duePayments);
        SaleTransactionsSearchRepository.saveAll(saleTransactions);
        return duePaymentMapper.toDto(duePayments);
    }

}
