package kr.bb.apigateway.store.filter;

import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.store.valueobject.StoreManagerStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Slf4j
//public class StoreAuthorizationGatewayFilter implements GlobalFilter {
//
//  @Override
//  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//    ServerHttpRequest request = exchange.getRequest();
//    String requestURI = request.getURI().toString();
//    log.warn("-------------requestURI :" +requestURI);
//
//    if (shouldNotFilter(requestURI)) {
//      chain.filter(exchange);
//    } else {
//      return roleHandler(exchange,chain);
//    }
//    return chain.filter(exchange);
//  }
//
//  private Mono<Void> roleHandler(ServerWebExchange exchange,GatewayFilterChain chain) {
//    String role = getRoleFromHeader(exchange);
//    if (StoreManagerStatus.ROLE_STORE_MANAGER_PENDING.name().equals(role)) {
//      return handlePendingApproval(exchange);
//    } else if (StoreManagerStatus.ROLE_STORE_MANAGER_DENIED.name().equals(role)) {
//      return handleDeniedRole(exchange);
//    }
//    return chain.filter(exchange);
//  }
//
//  private boolean shouldNotFilter(String requestURI) {
//    return !requestURI.contains("/stores") || requestURI.contains("/stores/login") ||
//        requestURI.contains("/stores/signup") || requestURI.contains("/stores/emails");
//  }
//
//  private String getRoleFromHeader(ServerWebExchange exchange) {
//    return ExtractAuthorizationTokenUtil.extractRole(exchange.getRequest());
//  }
//
//  private Mono<Void> handlePendingApproval(ServerWebExchange exchange) {
//    ServerHttpResponse response = exchange.getResponse();
//    response.setStatusCode(HttpStatus.UNAUTHORIZED);
//    return response.setComplete();
//  }
//
//  private Mono<Void> handleDeniedRole(ServerWebExchange exchange) {
//    ServerHttpResponse response = exchange.getResponse();
//    response.setStatusCode(HttpStatus.FORBIDDEN);
//    return response.setComplete();
//  }
//
//}
