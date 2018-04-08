package de.shellfire.vpn.service.osx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;

import org.slf4j.Logger;

import de.shellfire.vpn.Util;
import de.shellfire.vpn.client.ConnectionState;
import de.shellfire.vpn.types.Reason;

public class OpenVpnManagementClient implements Runnable {
  private static Logger log = Util.getLogger(OpenVpnManagementClient.class.getCanonicalName());
  private Socket client;
  private PrintWriter printWriter;
  private OSXVpnController controller;

  public OpenVpnManagementClient(OSXVpnController controller) {
    this.controller = controller;
  }

  public void run() {

    try {
      int port = 1399;
      java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);
      client = serverSocket.accept();

      serverSocket.close();

      printWriter = new PrintWriter(new OutputStreamWriter(
          client.getOutputStream()));

      BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(client.getInputStream()));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        this.append(line);
      }

    } catch (IOException e) {
      Util.handleException(e);
    }
  }

  public void sendMessage(String msg) throws IOException {
    if (printWriter != null) {
      printWriter.print(msg);
      printWriter.flush();
    }
  }

  void append(String line) throws IOException {
    if (line != null) {
      line = line.trim();
      if (line.length() > 0) {
        log.debug("MgmtConsole: " + line);
        this.parse(line);     
      }
    }
  }

  private void parse(String line) throws IOException {
    if (line.startsWith(">HOLD:Waiting for hold release")) {
      sendMessage("hold release\n");
      sendMessage("state on\n");
    } else if (line.startsWith(">STATE")) {
      String[] parts = line.split(",");
      if (parts.length >= 3) {
        if (parts[1].equals("CONNECTED") && parts[2].equals("SUCCESS")) {
          this.setStatus(ConnectionState.Connected, Reason.SuccesfulConnectDetected);
        }
      }
    } else if (line.startsWith(">FATAL:Cannot allocate TUN/TAP dev dynamically")) {
      this.setStatus(ConnectionState.Disconnected, Reason.TunTapDriverNotLoaded);
    }

  }

  public boolean isConnected() {
    return (client != null) && (client.isConnected());
  }

  private void setStatus(ConnectionState connectionState, Reason reason)
      throws RemoteException {
    if (connectionState == ConnectionState.Disconnected)
      controller.disconnect(reason);
    else
      this.controller.setConnectionState(connectionState, reason);
  }

  public void disconnect() throws IOException {
    sendMessage("signal SIGINT\n");
  }

}
