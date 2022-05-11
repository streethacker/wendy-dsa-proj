package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SimpleSocketClient {
  public static void main(String[] args) throws IOException {
    String serverHost = "127.0.0.1";
    int serverPort = 6666;
    try (Socket clientSocket = new Socket()) {
      clientSocket.connect(new InetSocketAddress(serverHost, serverPort));
      try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {
        out.println("hello, world");
        out.println("exit()");
      }
    }
  }
}
