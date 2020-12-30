package com.sohag.authserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("auth-server")
@Data
public class AuthorizationServerProperties {
    private Boolean isRedisStandalone;
    private String redisHost;
    private String redisPort;
    private String redisSocketLocation;
    private String redisDatabase;
}
