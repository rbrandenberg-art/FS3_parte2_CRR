package com.SmartLogix.Api_Gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * Filtro global de logging — se aplica a TODAS las rutas del Gateway.
 *
 * Responsabilidades:
 *  1. Asignar un ID único (X-Request-Id) a cada request entrante.
 *  2. Loguear método, path y timestamp al inicio.
 *  3. Loguear el status HTTP y tiempo de respuesta al final.
 *
 * Implementa GlobalFilter + Ordered para ejecutarse antes que
 * los filtros de ruta específicos (orden más alto = mayor prioridad).
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long inicio = Instant.now().toEpochMilli();

        // Generar un ID único por request para trazabilidad
        String requestId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Agregar X-Request-Id al request para que llegue al microservicio
        ServerHttpRequest requestConId = request.mutate()
                .header("X-Request-Id", requestId)
                .build();

        log.info("[GATEWAY ►] [{}] {} {} — desde: {}",
                requestId,
                request.getMethod(),
                request.getURI().getPath(),
                request.getRemoteAddress() != null
                        ? request.getRemoteAddress().getAddress().getHostAddress()
                        : "desconocido");

        return chain.filter(exchange.mutate().request(requestConId).build())
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    long duracion = Instant.now().toEpochMilli() - inicio;
                    log.info("[GATEWAY ◄] [{}] {} {} → {} ({}ms)",
                            requestId,
                            request.getMethod(),
                            request.getURI().getPath(),
                            response.getStatusCode(),
                            duracion);
                }));
    }

    @Override
    public int getOrder() {
        // Orden más bajo = se ejecuta primero entre los GlobalFilters
        return -1;
    }
}