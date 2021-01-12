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


    public static void startClient(String ip, int port) {
        new Thread(() -> runClient(ip, port)).start();
    }

    public static void startServer(int port) {
        new Thread(() -> runServer(port)).start();
    }

    private static void runClient(String ip, int port) {
        while (!stop) {
            try {
                Socket interpreter = new Socket(ip, port);
                PrintWriter out = new PrintWriter(interpreter.getOutputStream());
                while (!stop) {
//                    out.println(simX + "," + simY + "," + simZ);

                    out.flush();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                    }
                }
                out.close();
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
//							System.out.println(line);
//                            if (line.startsWith("set simX"))
//                                simX = Double.parseDouble(line.split(" ")[2]);
                        } catch (NumberFormatException e) {
                        }
                    }
                    in.close();
                    client.close();
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
