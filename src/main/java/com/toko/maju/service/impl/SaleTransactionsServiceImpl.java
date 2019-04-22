package com.toko.maju.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toko.maju.domain.Product;
import com.toko.maju.domain.SaleItem;
import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.domain.SequenceNumber;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.SaleItemRepository;
import com.toko.maju.repository.SaleTransactionsRepository;
import com.toko.maju.repository.SequenceNumberRepository;
import com.toko.maju.repository.UserRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.repository.search.SequenceNumberSearchRepository;
import com.toko.maju.security.SecurityUtils;
import com.toko.maju.service.SaleTransactionsService;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.service.mapper.SaleTransactionsMapper;
import com.toko.maju.web.rest.errors.InternalServerErrorException;

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

	@Autowired
	private final SequenceNumberRepository sequenceNumberRepository = null;

	@Autowired
	private final SequenceNumberSearchRepository sequenceNumberSearchRepository = null;

	@Autowired
	private final SaleItemRepository saleItemRepository = null;

	@Autowired
	private final ProductRepository productRepository = null;

	@Autowired
	private final ProductSearchRepository productSearchRepository = null;

	@Autowired
	private final UserRepository userRepository = null;

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
	 * @throws InternalServerErrorException
	 */
	@Override
	@Transactional
	public SaleTransactionsDTO save(SaleTransactionsDTO saleTransactionsDTO) throws Exception {
		log.debug("Request to save SaleTransactions : {}", saleTransactionsDTO);
		log.debug("dto items" + saleTransactionsDTO.getItems());
		SaleTransactions saleTransactions = saleTransactionsMapper.toEntity(saleTransactionsDTO);
		log.debug("entity items" + saleTransactions.getItems());

		String login = SecurityUtils.getCurrentUserLogin().get();
		log.debug("login: " + login);
		saleTransactions.setCreator(userRepository.findOneByLogin(login).get());
		log.debug("Creator: " + saleTransactions.getCreator());

		SequenceNumber currentInvoiceNo = sequenceNumberRepository.findByType("invoice");
		int currentValue = currentInvoiceNo.getNextValue();
		String noInvoice = generateInvoiceNo(currentValue);
		currentInvoiceNo.setNextValue(++currentValue);
		sequenceNumberRepository.save(currentInvoiceNo);
		sequenceNumberSearchRepository.save(currentInvoiceNo);

		saleTransactions.setNoInvoice(noInvoice);
		saleTransactions = saleTransactionsRepository.save(saleTransactions);

		Set<Product> products = saleItemRepository.findProductsBySale(saleTransactions.getId());

		products = updateQty(saleTransactions.getItems(), products);
		log.debug(" update product: {}", products);
		productRepository.saveAll(products);
		productSearchRepository.saveAll(products);
		SaleTransactionsDTO result = saleTransactionsMapper.toDto(saleTransactions);
		saleTransactionsSearchRepository.save(saleTransactions);
		return result;
	}

	private String generateInvoiceNo(int currentValue) {
		StringBuilder build = new StringBuilder();
		build.append("INV");
		build.append(String.format("%06d", currentValue));
		log.debug("new Invoice No: " + build.toString());
		return build.toString();
	}

	private Set<Product> updateQty(Set<SaleItem> items, Set<Product> products) throws InternalServerErrorException {
		products.forEach(product -> {
			items.forEach(item -> {
				if (item.getProduct().getId() == product.getId()) {
					if (item.getQuantity() > product.getStock()) {
						throw new RuntimeException("Product change");
					}
					product.setStock(product.getStock() - item.getQuantity());
				}
			});
		});
		return products;
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
