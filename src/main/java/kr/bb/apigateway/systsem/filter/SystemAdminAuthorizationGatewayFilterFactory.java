package kr.bb.apigateway.systsem.filter;

import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.valueobject.Role;
import kr.bb.apigateway.systsem.exception.SystemAdminAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SystemAdminAuthorizationGatewayFilterFactory extends
    AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {


  public SystemAdminAuthorizationGatewayFilterFactory() {
    super(NameConfig.class);
  }

  public SystemAdminAuthorizationGatewayFilterFactory(
      Class<NameConfig> configClass) {
    super(configClass);
  }

  @Override
  public GatewayFilter apply(NameConfig config) {
    return (exchange, chain) -> {
       if (!isSystemAdmin(exchange)) {
        return handleUnauthorized(exchange);
      }
      return chain.filter(exchange);
    };
  }


  private boolean isSystemAdmin(ServerWebExchange exchange) {
    String role = ExtractAuthorizationTokenUtil.extractRole(exchange.getRequest());
    return Role.ROLE_SYSTEM_ADMIN.name().equals(role);
  }

  private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    throw new SystemAdminAuthException("존재 하지 않는 시스템 어드민 유저입니다.");
  }


}