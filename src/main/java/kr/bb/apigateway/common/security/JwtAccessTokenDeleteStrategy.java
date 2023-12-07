package kr.bb.apigateway.common.security;


public interface JwtAccessTokenDeleteStrategy {
  public void invalidateAccessToken(String token);

}
