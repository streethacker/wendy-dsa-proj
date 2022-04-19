package multithread.threadpool;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LightweightThreadPool implements ThreadPoolService {

  private static final long defaultTaskPollingIntervalMillis = 10;
  private static final long defaultAwaitCheckIntervalMillis = 10;

  /** 任务队列 */
  private LinkedBlockingQueue<Runnable> taskQueue;

  /** 线程池状态，true-已关闭，false-运行中 */
  private AtomicBoolean isShutdown;

  /** 工作线程 */
  private ArrayList<PoolWorker> workers;

  /** 当任务队列中没有任务时，工作线程阻塞，间隔 {@code taskPollingIntervalMillis} 毫秒后，重新尝试从taskQueue获取任务 */
  private long taskPollingIntervalMillis;

  /** {@code awaitTermination} 调用后，遍历worker线程检查是否退出，两次检查之间的等待时间，单位毫秒 */
  private long awaitCheckIntervalMillis;

  public LightweightThreadPool(int poolSize) {
    this(poolSize, defaultTaskPollingIntervalMillis, defaultAwaitCheckIntervalMillis);
  }

  public LightweightThreadPool(
      int poolSize, long taskPollingIntervalMillis, long awaitCheckIntervalMillis) {
    this.taskQueue = new LinkedBlockingQueue<>();
    this.isShutdown = new AtomicBoolean(false);
    this.workers = new ArrayList<>(poolSize);
    this.taskPollingIntervalMillis = taskPollingIntervalMillis;
    this.awaitCheckIntervalMillis = awaitCheckIntervalMillis;
    for (int i = 0; i < poolSize; i++) {
      PoolWorker worker = new PoolWorker("LightweightThreadPool||thread[#" + i + "]");
      worker.start();
      workers.add(worker);
    }
  }

  @Override
  public synchronized void execute(Runnable runnable) {
    if (isShutdown.get()) {
      throw new ThreadPoolServiceException("Illegal thread pool state: already shutdown");
    }
    taskQueue.offer(runnable);
  }

  @Override
  public synchronized void shutdown() {
    isShutdown.set(true);
  }

  @Override
  public void awaitTermination(long timeout) throws AwaitTimeoutException, InterruptedException {
    if (!isShutdown.get()) {
      throw new ThreadPoolServiceException(
          "Illegal thread pool state: must shutdown the thread pool before calling awaitTermination");
    }
    long currentTimeMillis = System.currentTimeMillis();
    while (System.currentTimeMillis() - currentTimeMillis <= timeout) {
      boolean terminated = true;
      for (Thread t : workers) {
        if (t.isAlive()) {
          terminated = false;
          break;
        }
      }
      if (terminated) {
        return;
      }
      Thread.sleep(awaitCheckIntervalMillis);
    }
    throw new AwaitTimeoutException(
        "Unable to terminate the thread pool before the specified timeout: " + timeout + "ms");
  }

  @SuppressWarnings("SameParameterValue")
  private Runnable poll(long timeout, TimeUnit unit) {
    Runnable runnable = null;
    try {
      runnable = taskQueue.poll(timeout, unit);
    } catch (InterruptedException ignored) {
    }
    return runnable;
  }

  private class PoolWorker extends Thread {

    public PoolWorker(String name) {
      super(name);
    }

    @Override
    public void run() {
      while (!isShutdown.get() || !taskQueue.isEmpty()) {
        Runnable runnable = null;
        while ((runnable = poll(taskPollingIntervalMillis, TimeUnit.MILLISECONDS)) != null) {
          runnable.run();
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ThreadPoolService poolService = new LightweightThreadPool(5);
    for (int i = 0; i < 10; i++) {
      final int j = i;
      poolService.execute(
          () -> {
            try {
              Thread.sleep(1000);
              System.out.println(Thread.currentThread().getName() + " => " + "Number: " + j);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }
    poolService.shutdown();
    poolService.awaitTermination(3000);
  }
}
