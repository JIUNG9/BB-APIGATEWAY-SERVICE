package kr.bb.apigateway.common.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  public static String accessKey;
  public static String refreshKey;

  private JwtUtil() {

  }

  @Value("${encrypt.key.access}")
  private void setAccessKey(String accessKey) {
    JwtUtil.accessKey = accessKey;
  }

  @Value("${encrypt.key.refresh}")
  private void setRefreshKey(String refreshKey) {
    JwtUtil.refreshKey = refreshKey;
  }

  public static String extractSubject(String token) {
    return extractAccessTokenClaims(token).getSubject();
  }

  public static Claims extractAccessTokenClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(accessKey.getBytes()).build()
        .parseClaimsJws(token)
        .getBody();
  }

  public static boolean isTokenValid(String token) {
    try {
      extractAccessTokenClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage()) {
      };
    } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      throw new IllegalArgumentException("올바르지 않은 접근입니다.");
    }
  }

}
