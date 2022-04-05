package exception;

public class UserException extends RuntimeException {
  public UserException() {
    super();
  }

  public UserException(String message) {
    super(message);
  }

  public UserException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public UserException(Throwable throwable) {
    super(throwable);
  }

  public static void main(String[] args) {
    throw new UserException("user exception");
  }
}
