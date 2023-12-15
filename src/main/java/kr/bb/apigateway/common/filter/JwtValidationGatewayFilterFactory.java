package kr.bb.apigateway.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.util.JwtUtil;
import kr.bb.apigateway.common.util.RedisBlackListTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtValidationGatewayFilterFactory extends
    AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.NameConfig> {

  private final RedisBlackListTokenUtil redisBlackListTokenUtil;

  public JwtValidationGatewayFilterFactory(RedisBlackListTokenUtil redisBlackListTokenUtil) {
    super(NameConfig.class);
    this.redisBlackListTokenUtil = redisBlackListTokenUtil;
  }

  @Override
  public GatewayFilter apply(NameConfig config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      String token = ExtractAuthorizationTokenUtil.extractToken(request);
      if (redisBlackListTokenUtil.isTokenBlacklisted(token)) {
        return handleError(exchange, HttpStatus.UNAUTHORIZED);
      } else {
        try {
          JwtUtil.isTokenValid(token);
          return chain.filter(addUserIdHeaderAtRequest(exchange, JwtUtil.extractSubject(token)));
        } catch (ExpiredJwtException e) {
          return handleError(exchange, HttpStatus.UNAUTHORIZED);
        }
      }
    };
  }


  private Mono<Void> handleError(ServerWebExchange exchange, HttpStatus status) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(status);
    return response.setComplete();
  }

  private ServerWebExchange addUserIdHeaderAtRequest(ServerWebExchange exchange, String userId) {
    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
        .headers(httpHeaders -> httpHeaders.add("userId", userId))
        .build();

    return exchange.mutate()
        .request(modifiedRequest)
        .build();
  }

}