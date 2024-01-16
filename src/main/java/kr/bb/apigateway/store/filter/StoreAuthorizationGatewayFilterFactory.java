package kr.bb.apigateway.store.filter;


import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.store.exception.StoreManagerAuthException;
import kr.bb.apigateway.store.valueobject.StoreManagerStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class StoreAuthorizationGatewayFilterFactory extends
    AbstractGatewayFilterFactory<StoreAuthorizationGatewayFilterFactory.NameConfig> {


  public StoreAuthorizationGatewayFilterFactory() {
    super(NameConfig.class);
  }

  @Override
  public GatewayFilter apply(NameConfig config) {
    return this::roleHandler;
  }


  private Mono<Void> roleHandler(ServerWebExchange exchange, GatewayFilterChain chain) {
    String role = getRoleFromHeader(exchange);
    if (StoreManagerStatus.ROLE_STORE_MANAGER_PENDING.name().equals(role)) {
      return handlePendingApproval(exchange);
    } else if (StoreManagerStatus.ROLE_STORE_MANAGER_DENIED.name().equals(role)) {
      return handleDeniedRole(exchange);
    }
    return chain.filter(exchange);
  }

  private String getRoleFromHeader(ServerWebExchange exchange) {
    return ExtractAuthorizationTokenUtil.extractRole(exchange.getRequest());
  }

  private Mono<Void> handlePendingApproval(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    throw new StoreManagerAuthException("사용자의 권한이 대기 중입니다.");
  }

  private Mono<Void> handleDeniedRole(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.FORBIDDEN);
    throw new StoreManagerAuthException("사용자의 사업자 등록증이 거절되었습니다 재등록하세요.");
  }


}