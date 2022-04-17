package multithread.threadpool;

public interface ThreadPoolService {
  void execute(Runnable runnable) throws InterruptedException;

  void shutdown();

  void awaitTermination(long timeout) throws AwaitTimeoutException, InterruptedException;
}
