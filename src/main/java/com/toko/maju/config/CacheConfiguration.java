package com.toko.maju.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.toko.maju.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.toko.maju.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Supplier.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Supplier.class.getName() + ".products", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.CustomerProduct.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Customer.class.getName() + ".products", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.ProjectProduct.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Project.class.getName() + ".products", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.SaleTransactions.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.SaleItem.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.SaleTransactions.class.getName() + ".items", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.SequenceNumber.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.DuePayment.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.SaleTransactions.class.getName() + ".duePayments", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Unit.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.CancelTransaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.ReturnTransaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.ReturnItem.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.ReturnTransaction.class.getName() + ".returnItems", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.BadStockProduct.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.StockOrder.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.StockOrderRequest.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.StockOrderProcess.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.StockOrderReceive.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Purchase.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.PurchaseList.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Purchase.class.getName() + ".purchaseLists", jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.Gerai.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.GeraiConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.GeraiTransaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.toko.maju.domain.GeraiUpdateHistory.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
