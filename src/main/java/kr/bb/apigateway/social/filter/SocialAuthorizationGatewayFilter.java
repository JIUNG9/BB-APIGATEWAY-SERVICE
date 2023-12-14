package kr.bb.apigateway.social.filter;


import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.valueobject.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
//
//@Slf4j
//public class SocialAuthorizationGatewayFilter implements GlobalFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String requestURI = request.getURI().toString();
//        log.warn("-------------requestURI :" + requestURI);
//
//        if (shouldNotFilter(requestURI)) {
//            chain.filter(exchange);
//        } else if (!isAuthorizedUser(exchange)) {
//            return handleUnauthenticatedUser(exchange);
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private boolean shouldNotFilter(String requestURI) {
//        return !requestURI.contains("/social") || requestURI.contains("/social/login")
//            || requestURI.contains("/oauth2");
//    }
//
//    private boolean isAuthorizedUser(ServerWebExchange exchange) {
//        String role = ExtractAuthorizationTokenUtil.extractRole(exchange.getRequest());
//        return Role.ROLE_SOCIAL_USER.name().equals(role);
//    }
//
//    private Mono<Void> handleUnauthenticatedUser(ServerWebExchange exchange) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        return response.setComplete();
//    }
//}
