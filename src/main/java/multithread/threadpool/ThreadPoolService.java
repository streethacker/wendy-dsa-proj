package multithread.threadpool;

public interface ThreadPoolService {
  void execute(Runnable runnable);

  void shutdown();

  void awaitTermination(long timeout) throws AwaitTimeoutException, InterruptedException;
}
