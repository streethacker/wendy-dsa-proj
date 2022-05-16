package network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SimpleHttpServer {
  private int port;
  private int backlog;

  public static class Request {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;
    private byte[] body;

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
      return "Request{"
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

  public SimpleHttpServer(int port, int backlog) {
    if (port < 0 || port > 0xFFFF) {
      throw new IllegalArgumentException("Port value out of range: " + port);
    }
    if (port < 256) {
      throw new IllegalArgumentException(
          "Port number less than 256 is reserved by system: " + port);
    }
    if (backlog < 1) {
      backlog = 50;
    }
    this.port = port;
    this.backlog = backlog;
  }

  public void serve() {
    try (ServerSocket serverSocket = new ServerSocket(port, backlog)) {
      System.out.println("---------- Server started! ----------");
      while (true) {
        Socket connection = serverSocket.accept();
        handleConnection(connection);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleConnection(Socket connection) {
    Thread t =
        new Thread(
            () -> {
              try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
                String method = null;
                String uri = null;
                String version = null;
                Map<String, String> headers = new HashMap<>();

                String line;
                boolean isFirstLine = true;
                while (!isBlank(line = getLine(in))) {
                  if (isFirstLine) {
                    String[] partition = line.split(" ", 3);
                    method = partition[0];
                    uri = partition[1];
                    version = partition[2];
                    isFirstLine = false;
                    continue;
                  }
                  String[] partition = line.split(": ", 2);
                  headers.put(partition[0], partition[1]);
                }
                byte[] body = null;
                String length = headers.get("Content-Length");
                if (Objects.nonNull(length)) {
                  int bodySize = Integer.parseInt(length);
                  body = new byte[bodySize];
                  in.read(body);
                }
                Request request = new Request();
                if (Objects.isNull(method) || Objects.isNull(uri) || Objects.isNull(version)) {
                  throw new RuntimeException("Bad Request");
                }
                request.setMethod(method);
                request.setPath(uri);
                request.setVersion(version);
                request.setHeaders(headers);
                if (Objects.nonNull(body)) {
                  request.setBody(body);
                }
                handleRequest(request);
              } catch (IOException e) {
                throw new RuntimeException("Fail to fetch input stream from connection");
              }
            });
    t.start();
  }

  private void handleRequest(Request request) {
    System.out.println(request);
  }

  private String getLine(InputStream in) throws IOException {
    StringBuilder builder = new StringBuilder();
    byte[] tmp = new byte[] {'\0'};
    while (in.read(tmp) > 0) {
      char c = (char) tmp[0];
      if (c == '\r') {
        in.mark(1);
        if (in.read(tmp) > 0 && (char) tmp[0] == '\n') {
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

  public static void main(String[] args) {
    SimpleHttpServer server = new SimpleHttpServer(6666, 3);
    server.serve();
  }
}
