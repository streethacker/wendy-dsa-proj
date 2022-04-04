package multithread;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHostResolver2 {

  // 需要解析的域名列表
  private List<String> urls;
  // todo
  private Map<String, String> urlHosts;

  public ConcurrentHostResolver2() {
    urls = new ArrayList<>();
    urlHosts = new ConcurrentHashMap<>();
  }

  public ConcurrentHostResolver2(List<String> urls) {
    this.urls = urls;
    urlHosts = new ConcurrentHashMap<>();
  }

  public Map<String, String> doResolve() throws InterruptedException {
    List<Thread> workers = new ArrayList<>();
    for (String url : urls) {
      Thread t = new WorkerThread(url);
      t.start();
      workers.add(t);
    }
    // 阻塞等待所有的worker thread都执行完
    for (Thread t : workers) {
      t.join();
    }
    return urlHosts;
  }

  public void add(String url) {
    urls.add(url);
  }

  public static String getHostAddress(String url) throws UnknownHostException {
    InetAddress address = InetAddress.getByName(url);
    return address.getHostAddress();
  }

  // 工作线程
  @SuppressWarnings("DuplicatedCode")
  private class WorkerThread extends Thread {

    // 当前线程要解析的url
    private String url;

    public WorkerThread(String url) {
      this.url = url;
    }

    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName() + " start!");
      try {
        String host = getHostAddress(url);
        urlHosts.put(url, host);
      } catch (UnknownHostException e) {
        // 生产代码是不这么用
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + " finish!");
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ConcurrentHostResolver2 resolver2 = new ConcurrentHostResolver2();
    resolver2.add("www.baidu.com");
    resolver2.add("www.huolala.cn");
    resolver2.add("www.sina.com");
    Map<String, String> urlHosts = resolver2.doResolve();
    // key: 域名， value：ip地址
    // entry就是(key, value)键值对
    // entrySet() 方法返回一个map的键值对迭代器
    for (Map.Entry<String, String> entry : urlHosts.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }
  }
}
