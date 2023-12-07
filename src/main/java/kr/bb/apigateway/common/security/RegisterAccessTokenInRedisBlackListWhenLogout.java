package kr.bb.apigateway.common.security;


import kr.bb.apigateway.common.util.RedisBlackListTokenUtil;
import kr.bb.apigateway.common.valueobject.SecurityPolicyStaticValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegisterAccessTokenInRedisBlackListWhenLogout implements
    JwtAccessTokenDeleteStrategy {
  private final RedisBlackListTokenUtil redisBlackListTokenUtil;
  @Override
  public void invalidateAccessToken(String token) {
    redisBlackListTokenUtil.addTokenToBlacklist(token,
        Long.parseLong(SecurityPolicyStaticValue.REFRESH_EXPIRATION_TIME));
  }
}
