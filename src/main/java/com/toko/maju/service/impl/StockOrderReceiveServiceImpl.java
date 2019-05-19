package com.toko.maju.service.impl;

import com.toko.maju.domain.Product;
import com.toko.maju.domain.User;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.UserRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.security.SecurityUtils;
import com.toko.maju.service.StockOrderReceiveService;
import com.toko.maju.domain.StockOrderReceive;
import com.toko.maju.repository.StockOrderReceiveRepository;
import com.toko.maju.repository.search.StockOrderReceiveSearchRepository;
import com.toko.maju.service.dto.StockOrderReceiveDTO;
import com.toko.maju.service.mapper.StockOrderReceiveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StockOrderReceive.
 */
@Service
@Transactional
public class StockOrderReceiveServiceImpl implements StockOrderReceiveService {

    private final Logger log = LoggerFactory.getLogger(StockOrderReceiveServiceImpl.class);

    private final StockOrderReceiveRepository stockOrderReceiveRepository;

    private final StockOrderReceiveMapper stockOrderReceiveMapper;

    private final StockOrderReceiveSearchRepository stockOrderReceiveSearchRepository;

    @Autowired
    private final ProductRepository productRepository = null;

    @Autowired
    private final ProductSearchRepository productSearchRepository = null;

    @Autowired
    private final UserRepository userRepository = null;

    public StockOrderReceiveServiceImpl(StockOrderReceiveRepository stockOrderReceiveRepository, StockOrderReceiveMapper stockOrderReceiveMapper, StockOrderReceiveSearchRepository stockOrderReceiveSearchRepository) {
        this.stockOrderReceiveRepository = stockOrderReceiveRepository;
        this.stockOrderReceiveMapper = stockOrderReceiveMapper;
        this.stockOrderReceiveSearchRepository = stockOrderReceiveSearchRepository;
    }

    /**
     * Save a stockOrderReceive.
     *
     * @param stockOrderReceiveDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOrderReceiveDTO save(StockOrderReceiveDTO stockOrderReceiveDTO) {
        log.debug("Request to save StockOrderReceive : {}", stockOrderReceiveDTO);
        StockOrderReceive stockOrderReceive = stockOrderReceiveMapper.toEntity(stockOrderReceiveDTO);
        stockOrderReceive = stockOrderReceiveRepository.save(stockOrderReceive);
        StockOrderReceiveDTO result = stockOrderReceiveMapper.toDto(stockOrderReceive);
        stockOrderReceiveSearchRepository.save(stockOrderReceive);
        return result;
    }

    /**
     * Get all the stockOrderReceives.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderReceiveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOrderReceives");
        return stockOrderReceiveRepository.findAll(pageable)
            .map(stockOrderReceiveMapper::toDto);
    }


    /**
     * Get one stockOrderReceive by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StockOrderReceiveDTO> findOne(Long id) {
        log.debug("Request to get StockOrderReceive : {}", id);
        return stockOrderReceiveRepository.findById(id)
            .map(stockOrderReceiveMapper::toDto);
    }

    /**
     * Delete the stockOrderReceive by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOrderReceive : {}", id);
        stockOrderReceiveRepository.deleteById(id);
        stockOrderReceiveSearchRepository.deleteById(id);
    }

    /**
     * Search for the stockOrderReceive corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOrderReceiveDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StockOrderReceives for query {}", query);
        return stockOrderReceiveSearchRepository.search(queryStringQuery(query), pageable)
            .map(stockOrderReceiveMapper::toDto);
    }

    @Override
    public List<StockOrderReceiveDTO> saveAll(List<StockOrderReceiveDTO> stockOrderReceiveDTOS) {
        List<StockOrderReceive> result = stockOrderReceiveMapper.toEntity(stockOrderReceiveDTOS);

        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userRepository.findOneByLogin(login).get();

        result.forEach(it -> {
            if(it.getCreatedDate() == null){
                it.setCreatedDate(Instant.now());
            }

            if(it.getCreator() == null){
                it.setCreator(user);
            }
        });

        HashMap<String, Integer> productReceives = new HashMap<>();
        createProductReceives(productReceives, result);
        List<Product> products = productRepository.findByBarcodeIn(productReceives.keySet());
        if(!productReceives.isEmpty()){
            log.debug("found {} products", products.size());
            products.forEach(it -> it.setStock(it.getStock() + productReceives.get(it.getBarcode())));

            products = productRepository.saveAll(products);
            productSearchRepository.saveAll(products);
            log.debug("updated products: {}", products);
        }

        result = stockOrderReceiveRepository.saveAll(result);
        stockOrderReceiveSearchRepository.saveAll(result);

        return stockOrderReceiveMapper.toDto(result);
    }

    private void createProductReceives(HashMap<String, Integer> productReceives, List<StockOrderReceive> orders) {
        orders.forEach(it -> productReceives.put(it.getBarcode(), it.getQuantity()));
    }
}
