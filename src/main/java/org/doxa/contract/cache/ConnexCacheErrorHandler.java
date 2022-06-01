package org.doxa.contract.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * Handler class for exception handling on cache error.
 * Cache-related exception will be handled to avoid App Malfunction
 */
@Slf4j
public class ConnexCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
        log.error(e.getMessage());
        log.trace("", e);
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        log.error(e.getMessage());
        log.trace("", e);
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        log.error(e.getMessage());
        log.trace("", e);
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        log.error(e.getMessage());
        log.trace("", e);
    }
}