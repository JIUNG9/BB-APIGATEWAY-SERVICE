package kr.bb.apigateway.store.exception;

public class StoreManagerAuthException extends
    RuntimeException {

  public StoreManagerAuthException() {
  }

  public StoreManagerAuthException(String message) {
    super(message);
  }
}
