package multithread.threadpool;

public class AwaitTimeoutException extends ThreadPoolServiceException {

  public AwaitTimeoutException() {
    super();
  }

  public AwaitTimeoutException(String message) {
    super(message);
  }

  public AwaitTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }

  public AwaitTimeoutException(Throwable cause) {
    super(cause);
  }
}
