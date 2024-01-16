package kr.bb.apigateway.social.dto;

import kr.bb.apigateway.common.valueobject.AuthId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SocialLoginRequestCommand {
  private AuthId socialId;
  private String email;
  private String phoneNumber;
  private String nickname;
}
