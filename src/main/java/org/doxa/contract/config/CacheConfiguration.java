package org.doxa.contract.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.doxa.contract.cache.ConnexCacheErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {
    public static final String ENTITIES_SERVICE_CACHE = "EntitiesService";
    public static final String OAUTH_SERVICE_CACHE = "OauthService";
    public static final String AUTHORITY_CACHE = "AuthorityCache";
    public static final String LOCAL_CACHE = "Local-ContractService";
    public static final String PURCHASE_SERVICE = "PurchaseService";

    private final RedisCacheProperties redisCacheProperties;

    @Autowired
    public CacheConfiguration(RedisCacheProperties redisCacheProperties) {
        this.redisCacheProperties = redisCacheProperties;
    }

    @Bean
    RedisStandaloneConfiguration redisConfiguration() {
        return new RedisStandaloneConfiguration(redisCacheProperties.getHost(), redisCacheProperties.getPort());
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(redisConfiguration());
    }

    @Bean
    @Primary
    public RedisCacheConfiguration connexCacheConfig() {
        ObjectMapper mapper = new ObjectMapper();
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(mapper, null);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        mapper.registerModule(new JavaTimeModule());

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(redisCacheProperties.getTtlInSeconds()))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(mapper)));
    }

    @Bean
    public RedisCacheConfiguration authorityCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(1800))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

    }


    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(ENTITIES_SERVICE_CACHE, connexCacheConfig())
                .withCacheConfiguration(LOCAL_CACHE, connexCacheConfig())
                .withCacheConfiguration(AUTHORITY_CACHE, connexCacheConfig())
                .withCacheConfiguration(OAUTH_SERVICE_CACHE, connexCacheConfig());

    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new ConnexCacheErrorHandler();
    }

    @Bean
    KeyGenerator connexCacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder paramsSb = new StringBuilder();
            for (Object p : params) {
                if (p == null) {
                    p = "";
                }
                paramsSb.append(p.toString());
            }
            return target.getClass().getSimpleName() + method.getName() + paramsSb.toString();
        };
    }

    @Bean
    KeyGenerator authorityCacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder paramsSb = new StringBuilder();
            for (Object p : params) {
                if (p == null) {
                    p = "";
                }
                paramsSb.append(p);
            }
            return "authority" + paramsSb;
        };
    }
}
