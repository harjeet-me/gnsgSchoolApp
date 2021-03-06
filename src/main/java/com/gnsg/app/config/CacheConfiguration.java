package com.gnsg.app.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.gnsg.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.gnsg.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.gnsg.app.domain.User.class.getName());
            createCache(cm, com.gnsg.app.domain.Authority.class.getName());
            createCache(cm, com.gnsg.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.gnsg.app.domain.Student.class.getName());
            createCache(cm, com.gnsg.app.domain.Student.class.getName() + ".charges");
            createCache(cm, com.gnsg.app.domain.Charge.class.getName());
            createCache(cm, com.gnsg.app.domain.AppliedCharge.class.getName());
            createCache(cm, com.gnsg.app.domain.AppliedCharge.class.getName() + ".students");
            createCache(cm, com.gnsg.app.domain.Student.class.getName() + ".appliedCharges");
            createCache(cm, com.gnsg.app.domain.Program.class.getName());
            createCache(cm, com.gnsg.app.domain.ASPath.class.getName());
            createCache(cm, com.gnsg.app.domain.ASPath.class.getName() + ".pRouls");
            createCache(cm, com.gnsg.app.domain.Expense.class.getName());
            createCache(cm, com.gnsg.app.domain.Revenue.class.getName());
            createCache(cm, com.gnsg.app.domain.Task.class.getName());
            createCache(cm, com.gnsg.app.domain.Sevadar.class.getName());
            createCache(cm, com.gnsg.app.domain.Sevadar.class.getName() + ".programs");
            createCache(cm, com.gnsg.app.domain.PRoul.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
