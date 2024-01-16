package kr.bb.apigateway.common.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import kr.bb.apigateway.common.valueobject.SecurityPolicyStaticValue;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GatewayTokenHandlerUtil {

  private GatewayTokenHandlerUtil(){

  }


    public static ServerWebExchange addUserIdHeaderAtRequest(ServerWebExchange exchange, String userId) {
    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
        .headers(httpHeaders -> httpHeaders.add("userId", userId))
        .build();

    return exchange.mutate()
        .request(modifiedRequest)
        .build();
  }


    public static Mono<Void> handleExpiredToken(ExpiredJwtException exception, ServerWebExchange exchange,
      HttpStatus status) {
    ServerHttpResponse response = exchange.getResponse();

    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    byte[] errorDto = expiredErrorDtoByte(exception);
    DataBuffer buffer = response.bufferFactory().wrap(errorDto);

    response.setStatusCode(status);
    return response.writeWith(Mono.just(buffer));
  }

  public static byte[] expiredErrorDtoByte(ExpiredJwtException exception) {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> jsonResponse = new HashMap<>();
    jsonResponse.put("message", "Expired");
    jsonResponse.put("id", Long.valueOf(exception.getClaims().getSubject()));
    jsonResponse.put("role",
        exception.getClaims().get(SecurityPolicyStaticValue.CLAIMS_ROLE_KEY_NAME, String.class));

    byte[] responseBytes;
    try {
      responseBytes = mapper.writeValueAsBytes(jsonResponse);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("토큰 만료 반환 데이터 Json Error");
    }
    return responseBytes;
  }
}
