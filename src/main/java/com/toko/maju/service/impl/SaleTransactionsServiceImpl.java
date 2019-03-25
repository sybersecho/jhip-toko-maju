package com.toko.maju.service.impl;

import com.toko.maju.service.SaleTransactionsService;
import com.toko.maju.domain.SaleItem;
import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.repository.SaleTransactionsRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.service.mapper.SaleTransactionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SaleTransactions.
 */
@Service
@Transactional
public class SaleTransactionsServiceImpl implements SaleTransactionsService {

	private final Logger log = LoggerFactory.getLogger(SaleTransactionsServiceImpl.class);

	private final SaleTransactionsRepository saleTransactionsRepository;

	private final SaleTransactionsMapper saleTransactionsMapper;

	private final SaleTransactionsSearchRepository saleTransactionsSearchRepository;

	public SaleTransactionsServiceImpl(SaleTransactionsRepository saleTransactionsRepository,
			SaleTransactionsMapper saleTransactionsMapper,
			SaleTransactionsSearchRepository saleTransactionsSearchRepository) {
		this.saleTransactionsRepository = saleTransactionsRepository;
		this.saleTransactionsMapper = saleTransactionsMapper;
		this.saleTransactionsSearchRepository = saleTransactionsSearchRepository;
	}

	/**
	 * Save a saleTransactions.
	 *
	 * @param saleTransactionsDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public SaleTransactionsDTO save(SaleTransactionsDTO saleTransactionsDTO) {
		log.debug("Request to save SaleTransactions : {}", saleTransactionsDTO);
		SaleTransactions saleTransactions = saleTransactionsMapper.toEntity(saleTransactionsDTO);
//		log.debug("Request to save entity SaleTransactions : {}", saleTransactions);
//		Set<SaleItem> items = saleTransactions.getItems();
//		saleTransactions.setItems(new HashSet<SaleItem>());
//		final SaleTransactions finalSale = saleTransactions;
//		if (items.isEmpty()) {
//			log.debug("item is empyt");
//		}
//		finalSale.getItems().forEach(item -> {
//			item.setSale(finalSale);
//			log.debug("sale is {}", item);
//			finalSale.getItems().add(item);
//		});
//		log.debug("Final SaleTransactions : {}", finalSale);
		saleTransactions = saleTransactionsRepository.save(saleTransactions);
//		log.debug(sale);
		SaleTransactionsDTO result = saleTransactionsMapper.toDto(saleTransactions);
		saleTransactionsSearchRepository.save(saleTransactions);
		return result;
	}

	/**
	 * Get all the saleTransactions.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<SaleTransactionsDTO> findAll(Pageable pageable) {
		log.debug("Request to get all SaleTransactions");
		return saleTransactionsRepository.findAll(pageable).map(saleTransactionsMapper::toDto);
	}

	/**
	 * Get one saleTransactions by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<SaleTransactionsDTO> findOne(Long id) {
		log.debug("Request to get SaleTransactions : {}", id);
		return saleTransactionsRepository.findById(id).map(saleTransactionsMapper::toDto);
	}

	/**
	 * Delete the saleTransactions by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete SaleTransactions : {}", id);
		saleTransactionsRepository.deleteById(id);
		saleTransactionsSearchRepository.deleteById(id);
	}

	/**
	 * Search for the saleTransactions corresponding to the query.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<SaleTransactionsDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of SaleTransactions for query {}", query);
		return saleTransactionsSearchRepository.search(queryStringQuery(query), pageable)
				.map(saleTransactionsMapper::toDto);
	}
}
