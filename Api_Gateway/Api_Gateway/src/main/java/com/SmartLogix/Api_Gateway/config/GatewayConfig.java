package com.SmartLogix.Api_Gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de rutas del API Gateway.
 *
 * Cada ruta define:
 *  - path()  → el patrón de URL que recibe el cliente
 *  - filters → transformaciones aplicadas al request/response
 *  - uri()   → el microservicio destino
 *
 * Flujo de una petición:
 *   Cliente → :8080/api/inventario/** → Gateway → :8081/api/inventario/**
 *   Cliente → :8080/api/pedidos/**   → Gateway → :8082/api/pedidos/**
 *   Cliente → :8080/api/envios/**    → Gateway → :8083/api/envios/**
 *   Cliente → :8080/api/usuarios/**  → Gateway → :8084/api/usuarios/**
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()

            // ── Inventario ────────────────────────────────────────────────────
            .route("inventario-route", r -> r
                .path("/api/inventario/**")
                .filters(f -> f
                    .addRequestHeader("X-Gateway-Source", "ecommerce-gateway")
                    .addResponseHeader("X-Servicio", "inventario")
                )
                // ANTES: .uri("http://localhost:8081")
                .uri("http://inventario-service:8081") 
            )

            // ── Pedido ───────────────────────────────────────────────────────
            .route("pedido-route", r -> r
                .path("/api/pedidos/**")
                .filters(f -> f
                    .addRequestHeader("X-Gateway-Source", "ecommerce-gateway")
                    .addResponseHeader("X-Servicio", "pedido")
                )
                // ANTES: .uri("http://localhost:8082")
                .uri("http://pedido-service:8082")
            )

            // ── Envíos ───────────────────────────────────────────────────────
            .route("envios-route", r -> r
                .path("/api/envios/**")
                .filters(f -> f
                    .addRequestHeader("X-Gateway-Source", "ecommerce-gateway")
                    .addResponseHeader("X-Servicio", "envios")
                )
                // ANTES: .uri("http://localhost:8083")
                .uri("http://envio-service:8083")
            )

            // ── Usuario ──────────────────────────────────────────────────────
            .route("usuario-route", r -> r
                .path("/api/usuarios/**")
                .filters(f -> f
                    .addRequestHeader("X-Gateway-Source", "ecommerce-gateway")
                    .addResponseHeader("X-Servicio", "usuario")
                )
                // ANTES: .uri("http://localhost:8084")
                .uri("http://usuario-service:8084")
            )

        .build();
    }
}
