package multithread.threadpool;

public class ThreadPoolServiceException extends RuntimeException {

  public ThreadPoolServiceException() {
    super();
  }

  public ThreadPoolServiceException(String message) {
    super(message);
  }

  public ThreadPoolServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ThreadPoolServiceException(Throwable cause) {
    super(cause);
  }
}
