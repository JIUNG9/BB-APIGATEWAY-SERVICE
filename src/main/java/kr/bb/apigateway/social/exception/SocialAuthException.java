package kr.bb.apigateway.social.exception;

import lombok.Getter;


@Getter
public class SocialAuthException extends RuntimeException{

  public SocialAuthException(String message) {
    super(message);
  }

  public SocialAuthException(String message, Throwable cause) {
    super(message, cause);
  }
}
