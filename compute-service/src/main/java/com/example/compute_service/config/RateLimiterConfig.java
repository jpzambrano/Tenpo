package com.example.compute_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;

@Configuration
@Slf4j
public class RateLimiterConfig {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterConfig.class);

    @Bean
    public KeyResolver keyResolver() {
    return exchange -> Mono.just(
         Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
                                    .orElseGet(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                                                             .map(InetSocketAddress::getAddress)
                                                             .map(InetAddress::getHostAddress)
                                                             .orElse("unknown"))
    );

    
                    
}   
}
    