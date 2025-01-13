package com.example.compute_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;

@Configuration
public class RateLimiterConfig  {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterConfig.class);
  /**
     * Define un RedisRateLimiter con un replenishRate de 3 y un burstCapacity de 5.
     * Esto configura cuántas solicitudes se permiten por segundo y la capacidad de ráfaga máxima.
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(3, 5); // Ajusta los valores según tus necesidades
    }

    /**
     * Configura un KeyResolver que utiliza la dirección IP del cliente como clave.
     * Esto asegura que el Rate Limiting se aplique por cliente (basado en IP).
     */
    @Bean
    public KeyResolver limitResolver() {
        log.info("KeyResolver bean created");
        return exchange -> {
            String clientKey = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                                        .map(InetSocketAddress::getAddress)
                                        .map(InetAddress::getHostAddress)
                                        .orElse("unknown");
            log.info("Client Key: {}", clientKey);
            return Mono.just(clientKey);
        };  
    }
}
    