package network;

import sun.misc.Signal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleHttpServer {

  private static final String[] SIGNALS = new String[] {"TERM", "HUP", "INT"};
  private static final List<String> METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE");

  private final int port;
  private final int backlog;
  private boolean isAlive;
  private List<Thread> workerThreads;

  public static class HttpRequest {

    private String method;

    private String path;

    private String version;

    private Map<String, String> headers;

    private byte[] body;

    public HttpRequest() {
      headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
      headers.put(key, value);
    }

    public String getHeader(String key, String defaultValue) {
      String value = headers.get(key);
      if (null != value) {
        return value;
      }
      return defaultValue;
    }

    public String getMethod() {
      return method;
    }

    public void setMethod(String method) {
      this.method = method;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public Map<String, String> getHeaders() {
      return headers;
    }

    public void setHeaders(Map<String, String> headers) {
      this.headers = headers;
    }

    public byte[] getBody() {
      return body;
    }

    public void setBody(byte[] body) {
      this.body = body;
    }

    @Override
    public String toString() {
      return "HttpRequest{"
          + "method='"
          + method
          + '\''
          + ", path='"
          + path
          + '\''
          + ", version='"
          + version
          + '\''
          + ", headers="
          + headers
          + ", body="
          + new String(body)
          + '}';
    }
  }

  public static class HttpResponse {

    private String version;

    private String status;

    private String description;

    private Map<String, String> headers;

    private byte[] body;

    public void addHeader(String key, String value) {
      headers.put(key, value);
    }

    public String getHeader(String key, String defaultValue) {
      String value = headers.get(key);
      if (null != value) {
        return value;
      }
      return defaultValue;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Map<String, String> getHeaders() {
      return headers;
    }

    public void setHeaders(Map<String, String> headers) {
      this.headers = headers;
    }

    public byte[] getBody() {
      return body;
    }

    public void setBody(byte[] body) {
      this.body = body;
    }
  }

  public SimpleHttpServer(int port, int backlog) {
    if (port < 0 || port > 0xFFFF) {
      throw new IllegalArgumentException("Port value out of range: " + port);
    }
    if (port < 256) {
      throw new IllegalArgumentException(
          "Port numbers less than 256 are reserved by the system: " + port);
    }
    if (backlog < 1) {
      backlog = 50;
    }
    this.port = port;
    this.backlog = backlog;
    this.workerThreads = new CopyOnWriteArrayList<>();
    registerSignalHandler();
  }

  private void registerSignalHandler() {
    for (String signal : SIGNALS) {
      Signal.handle(
          new Signal(signal),
          sig -> {
            System.out.println("Handling Signal: " + sig);
            setAlive(false);
            for (Thread t : workerThreads) {
              t.interrupt();
            }
          });
    }
  }

  public void serve() {
    try (ServerSocket serverSocket = new ServerSocket(port, backlog)) {
      System.out.println("---------- Server Started ----------");
      isAlive = true;
      while (isAlive()) {
        Socket connection = serverSocket.accept();
        Thread t = handleConnection(connection);
        workerThreads.add(t);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Thread handleConnection(Socket connection) {
    Thread t =
        new Thread(
            () -> {
              try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
                HttpRequest request = new HttpRequest();
                byte[] body;
                String line;
                boolean isFirstLine = true;
                while (!isBlank(line = getLine(in))) {
                  if (isFirstLine) {
                    isFirstLine = false;
                    String[] partition = line.split(" ", 3);
                    request.setMethod(partition[0]);
                    request.setPath(partition[1]);
                    request.setVersion(partition[2]);
                    continue;
                  }
                  String[] partition = line.split(": ", 2);
                  request.addHeader(partition[0], partition[1]);
                }
                int bodySize = Integer.parseInt(request.getHeader("Content-Length", "-1"));
                if (bodySize > 0) {
                  body = new byte[bodySize];
                  in.read(body);
                  request.setBody(body);
                }
                handleRequest(request, connection);
              } catch (IOException e) {
                throw new RuntimeException("Fail to read from the connection.");
              }
            });
    t.start();
    return t;
  }

  private void handleRequest(HttpRequest request, Socket connection) {
    System.out.println(request);
  }

  private String getLine(InputStream in) throws IOException {
    StringBuilder builder = new StringBuilder();
    char c;
    byte[] tmp = new byte[] {'\0'};
    while (in.read(tmp) > 0) {
      c = (char) (tmp[0] & 0xFF);
      if (c == '\r') {
        in.mark(1);
        if (in.read(tmp) > 0 && (char) (tmp[0] & 0xFF) == '\n') {
          break;
        }
        in.reset();
      }
      builder.append(c);
    }
    return builder.toString();
  }

  private boolean isBlank(String str) {
    int strLen;
    if (str != null && (strLen = str.length()) != 0) {
      for (int i = 0; i < strLen; ++i) {
        if (!Character.isWhitespace(str.charAt(i))) {
          return false;
        }
      }
    }
    return true;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public int getPort() {
    return port;
  }

  public int getBacklog() {
    return backlog;
  }

  public static void main(String[] args) {
    SimpleHttpServer server = new SimpleHttpServer(6666, 3);
    server.serve();
  }
}
