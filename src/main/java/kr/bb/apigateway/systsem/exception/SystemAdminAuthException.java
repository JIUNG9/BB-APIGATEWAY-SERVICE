package kr.bb.apigateway.systsem.exception;

public class SystemAdminAuthException extends
    RuntimeException {

  public SystemAdminAuthException() {
  }

  public SystemAdminAuthException(String message) {
    super(message);
  }
}
