package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

public class Simulator {

    private static volatile boolean stop;
    private static PrintWriter outClient = null;

    public static void startClient(String ip, int port) {
        new Thread(() -> runClient(ip, port)).start();
    }

    public static void startServer(int port) {
        new Thread(() -> runServer(port)).start();
    }

    public static void sentToServer(String[] lines) {
        if (outClient == null)
            return;

        for (String line : lines) {
            outClient.println(line);
            outClient.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sentToServer(String line) {
        if (outClient == null)
            return;
        outClient.println(line);
        outClient.flush();
    }

    private static void runClient(String ip, int port) {
        while (!stop) {
            try {
                Socket interpreter = new Socket(ip, port);
                outClient = new PrintWriter(interpreter.getOutputStream());
                while (!stop) {
//                    out.println(simX + "," + simY + "," + simZ);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                    }
                }
                outClient.close();
                interpreter.close();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    private static void runServer(int port) {
        try {
            double aileron = 0;
            double elevator = 0;
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            System.out.print("Simulator server: Waiting for clients\n");
            while (!stop) {
                try {
                    System.out.print(".");
                    Socket client = server.accept();
                    System.out.println("Simulator server: Client Connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line = null;
                    while (!(line = in.readLine()).equals("bye")) {
                        try {
                            System.out.println("Simulator server: " + line);
//                            if (line.startsWith("set simX"))
//                                simX = Double.parseDouble(line.split(" ")[2]);
                        } catch (NumberFormatException e) {
                        }
                    }
                    in.close();
                    client.close();
                    System.out.println("Simulator server: Client DisConnected");
                } catch (SocketTimeoutException e) {
                }
            }
            server.close();
        } catch (IOException e) {
        }
    }

    public void close() {
        stop = true;
    }
}
