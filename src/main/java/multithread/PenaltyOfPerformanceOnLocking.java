package multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Thread-0 acquire lock, at: 1651565963341
Thread-1 acquire lock, at: 1651565963341
Thread-0 get lock, at: 1651565963341
Thread-2 acquire lock, at: 1651565963341
Thread-3 acquire lock, at: 1651565963341
Thread-4 acquire lock, at: 1651565963341
Thread-5 acquire lock, at: 1651565963341

# 实际上 Thread-6 在41就申请了锁
Thread-6 acquire lock, at: 1651565963341


Thread-7 acquire lock, at: 1651565963341
Thread-8 acquire lock, at: 1651565963341
Thread-9 acquire lock, at: 1651565963341
Thread-0 release lock, at: 1651565963343
Thread-1 get lock, at: 1651565963343
Thread-1 release lock, at: 1651565963350
Thread-2 get lock, at: 1651565963350
Thread-2 release lock, at: 1651565963357
Thread-3 get lock, at: 1651565963357
Thread-3 release lock, at: 1651565963363
Thread-4 get lock, at: 1651565963363
Thread-4 release lock, at: 1651565963370
Thread-5 get lock, at: 1651565963370

# Thread-5释放锁之后，隔了1ms， Thread-6才拿到锁
Thread-5 release lock, at: 1651565963370
Thread-6 get lock, at: 1651565963371

# Thread-1在71释放了锁
Thread-6 release lock, at: 1651565963371


Thread-7 get lock, at: 1651565963371
Thread-7 release lock, at: 1651565963371
Thread-8 get lock, at: 1651565963371
Thread-8 release lock, at: 1651565963371
Thread-9 get lock, at: 1651565963371
Thread-9 release lock, at: 1651565963371
*/
public class PenaltyOfPerformanceOnLocking {
  public static void main(String[] args) throws InterruptedException {
    Lock lock = new ReentrantLock();
    Runnable runnable =
        () -> {
          System.out.println(
              Thread.currentThread().getName()
                  + " acquire lock, at: "
                  + System.currentTimeMillis());
          lock.lock();
          try {
            System.out.println(
                Thread.currentThread().getName() + " get lock, at: " + System.currentTimeMillis());
            int i = 0;
            while (i < 1000000) {
              i++;
            }
          } finally {
            lock.unlock();
            System.out.println(
                Thread.currentThread().getName()
                    + " release lock, at: "
                    + System.currentTimeMillis());
          }
        };
    List<Thread> threadList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      threadList.add(new Thread(runnable, "Thread-" + i));
    }
    for (Thread t : threadList) {
      t.start();
    }
    for (Thread t : threadList) {
      t.join();
    }
  }
}
