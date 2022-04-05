package multithread;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("UnusedReturnValue")
public class ConcurrentHostResolver {

  private List<String> urls;
  private Map<String, String> urlHosts;

  public ConcurrentHostResolver() {
    urls = new ArrayList<>();
    urlHosts = new ConcurrentHashMap<>();
  }

  public Map<String, String> doResolve() throws InterruptedException {
    List<Thread> workers = new ArrayList<>();
    for (String url : urls) {
      Thread t = new ResolverWorker(url);
      workers.add(t);
      t.start();
    }
    for (Thread t : workers) {
      t.join();
    }
    return urlHosts;
  }

  public int add(String url) {
    urls.add(url);
    return urls.size();
  }

  public int batchAdd(List<String> urls) {
    this.urls.addAll(urls);
    return urls.size();
  }

  public static String getHostAddress(String url) throws UnknownHostException {
    InetAddress address = InetAddress.getByName(url);
    return address.getHostAddress();
  }

  private class ResolverWorker extends Thread {

    private final String url;

    public ResolverWorker(String url) {
      this.url = url;
    }

    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName() + " start!");
      try {
        String host = getHostAddress(url);
        urlHosts.put(url, host);
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + " finish!");
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ConcurrentHostResolver resolver = new ConcurrentHostResolver();
    resolver.add("www.baidu.com");
    resolver.add("www.sina.com");
    resolver.add("www.liaoxuefeng.com");
    Map<String, String> urlHosts = resolver.doResolve();
    for (Map.Entry<String, String> entry : urlHosts.entrySet()) {
      System.out.println("url => " + entry.getKey() + ", " + "host => " + entry.getValue());
    }
  }
}
