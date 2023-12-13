package kr.bb.apigateway.systsem.filter;

import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.valueobject.Role;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SystemAdminAuthorizationGatewayFilter implements GlobalFilter {
   @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestURI = request.getURI().getPath();

        if (shouldNotFilter(requestURI)) {
            return chain.filter(exchange);
        }
        else if (!isSystemAdmin(exchange)) {
            return handleUnauthorized(exchange);
        }
        return chain.filter(exchange);
    }

    private boolean shouldNotFilter(String requestURI) {
        return !requestURI.contains("/admin") || requestURI.contains("/admin/login");
    }

    private boolean isSystemAdmin(ServerWebExchange exchange) {
        String role = ExtractAuthorizationTokenUtil.extractRole(exchange.getRequest());
        return Role.ROLE_SYSTEM_ADMIN.name().equals(role);
    }


    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
