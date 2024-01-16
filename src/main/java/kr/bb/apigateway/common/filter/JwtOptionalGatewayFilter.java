package kr.bb.apigateway.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import kr.bb.apigateway.common.dto.GatewayTokenHandlerUtil;
import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.util.JwtUtil;
import kr.bb.apigateway.common.util.RedisBlackListTokenUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtOptionalGatewayFilter extends
    AbstractGatewayFilterFactory<JwtOptionalGatewayFilter.NameConfig> {

  private final RedisBlackListTokenUtil redisBlackListTokenUtil;

  public JwtOptionalGatewayFilter(RedisBlackListTokenUtil redisBlackListTokenUtil) {
    super(NameConfig.class);
    this.redisBlackListTokenUtil = redisBlackListTokenUtil;
  }

  @Override
  public GatewayFilter apply(NameConfig config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (ExtractAuthorizationTokenUtil.isThereToken(request)) {
        return handleWhenThereIsToken(exchange, chain);
      } else {
        return chain.filter(exchange);
      }
    };
  }

  private void checkTokenIsBlackListed(String token) {
    if (redisBlackListTokenUtil.isTokenBlacklisted(token)) {
      throw new IllegalArgumentException("로그아웃 처리된 토큰은 사용할 수 없습니다.");
    }
  }

  private Mono<Void> handleWhenThereIsToken(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    String token = ExtractAuthorizationTokenUtil.extractToken(request);
    checkTokenIsBlackListed(token);

    try {
      JwtUtil.isTokenValid(token);
      return chain.filter(
          GatewayTokenHandlerUtil.addUserIdHeaderAtRequest(exchange,
              JwtUtil.extractSubject(token)));
    } catch (ExpiredJwtException e) {
      return GatewayTokenHandlerUtil.handleExpiredToken(e, exchange, HttpStatus.UNAUTHORIZED);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("올바르지 않은 토큰입니다.");
    }
  }


}