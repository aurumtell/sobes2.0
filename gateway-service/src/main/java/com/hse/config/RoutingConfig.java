package com.hse.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        .uri("http://auth-server:8082"))
                .route(r -> r.path("/feedback/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://auth-server:8082"))
                .route(r -> r.path("/user/logout")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://auth-server:8082"))
                .route(r -> r.path("/user/article/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://content-server:8083"))
                .route(r -> r.path("/user/advise/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://content-server:8083"))
                .route(r -> r.path("/user/profile/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://auth-server:8082"))
                .route(r -> r.path("/user/profiles/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://auth-server:8082"))
                .route(r -> r.path("/user/interview/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://interview-server:8084"))
                .route(r -> r.path("/ws/chat/**")
                        .filters(f -> f.filter(addAuthHeader())) // WebSocket route
                        .uri("ws://chat-server:8085"))
                .route(r -> r.path("/chat/**")
                        .filters(f -> f.filter(addAuthHeader()))
                        .uri("http://chat-server:8085"))
                .build();
    }

    private GatewayFilter addAuthHeader() {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var headers = request.getHeaders();
            var authHeader = headers.getFirst("Authorization");

            var newRequest = request.mutate()
                    .header("Authorization", authHeader)
                    .build();

            var newExchange = exchange.mutate().request(newRequest).build();

            return chain.filter(newExchange);
        };
    }
}
