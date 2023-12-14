package kr.bb.apigateway.common.util;

import io.jsonwebtoken.Claims;
import kr.bb.apigateway.common.valueobject.SecurityPolicyStaticValue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class ExtractAuthorizationTokenUtil {


  private ExtractAuthorizationTokenUtil() {

  }

  public static String extractToken(ServerHttpRequest request) {
    HttpHeaders headers = request.getHeaders();
    String authorizationHeader = headers.getFirst(
        SecurityPolicyStaticValue.TOKEN_AUTHORIZAION_HEADER_NAME);
    if (authorizationHeader != null && authorizationHeader.startsWith(
        SecurityPolicyStaticValue.TOKEN_AUTHORIZATION_PREFIX)) {
      return authorizationHeader.substring(7);
    } else {
      throw new IllegalArgumentException("토큰 정보를 찾을 수 없습니다. 로그인을 먼저 해주세요.");
    }
  }

  public static String extractUserId(ServerHttpRequest request) {
    String token = extractToken(request);
    return JwtUtil.extractSubject(token);
  }

    public static String extractRole(ServerHttpRequest request) {
    String token = ExtractAuthorizationTokenUtil.extractToken(request);
    Claims claims = JwtUtil.extractClaims(token);
    return (String) claims.get(SecurityPolicyStaticValue.CLAIMS_ROLE_KEY_NAME);
  }

}
