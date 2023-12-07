package kr.bb.apigateway.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.util.JwtUtil;
import kr.bb.apigateway.common.util.RedisBlackListTokenUtil;
import kr.bb.apigateway.common.valueobject.JWTAuthenticationShouldNotFilterAntMatcher;
import kr.bb.apigateway.common.valueobject.SwaggerRequestURI;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class JwtValidationGatewayFilter implements GlobalFilter {

  private final RedisBlackListTokenUtil redisBlackListTokenUtil;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String requestURI = request.getURI().getPath();

    if (shouldNotFilter(requestURI)) {
      return chain.filter(exchange);
    }

    String token = ExtractAuthorizationTokenUtil.extractToken(exchange.getRequest());
    if (redisBlackListTokenUtil.isTokenBlacklisted(token)) {
      ServerHttpResponse response = exchange.getResponse();
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return response.setComplete();
    }

    try {
      JwtUtil.isTokenValid(token);
    } catch (ExpiredJwtException e) {
      ServerHttpResponse response = exchange.getResponse();
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return response.setComplete();
    }

    return chain.filter(addUserIdHeaderAtRequest(exchange,JwtUtil.extractSubject(token)));
  }

  private boolean shouldNotFilter(String requestURI) {
    return requestURI.contains(SwaggerRequestURI.UI_URI) ||
        requestURI.contains(SwaggerRequestURI.API_DOCS_URI) ||
        requestURI.contains(JWTAuthenticationShouldNotFilterAntMatcher.EMAIL_ANT);
  }

  private ServerWebExchange addUserIdHeaderAtRequest(ServerWebExchange exchange, String userId) {
    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
        .header("userId", userId)
        .build();

    ServerWebExchange modifiedExchange = exchange.mutate()
        .request(modifiedRequest)
        .build();

    return modifiedExchange;
  }
}