package org.doxa.contract.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "doxa.redis")
public class RedisCacheProperties {
    private String host = "localhost";
    private int port = 6379;
    private int ttlInSeconds = 900;
}
