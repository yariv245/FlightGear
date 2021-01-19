package servers;

import javafx.beans.property.DoubleProperty;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GUIServer {
//    public static DoubleProperty airplaneX;
//    public static DoubleProperty airplaneY;
    private static volatile boolean stopServer;
    public static void startServer(int port) {
        new Thread(() -> runServer(port)).start();
    }

    private static void runServer(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            System.out.print("GUI server: Waiting for clients\n");
            while (!stopServer) {
                try {
                    System.out.print(".");
                    Socket clientFlightGear = server.accept();
//                    Socket clientGuiModel = server.accept();
                    System.out.println("GUI server: Client Connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientFlightGear.getInputStream()));
                    String line;
                    while (!(line = in.readLine()).equals("bye")) {
                        try {
                            //System.out.println("GUI server: " + line);
                            //Send line to model


//                            if (line.startsWith("set simX"))
//                                simX = Double.parseDouble(line.split(" ")[2]);
                        } catch (NumberFormatException e) {
                        }
                    }
                    in.close();
                    clientFlightGear.close();
                    System.out.println("GUI server: Client DisConnected");
                } catch (SocketTimeoutException e) {
                }
            }
            server.close();
        } catch (IOException e) {
        }
    }

    public void close() {
        stopServer = true;
    }

    public Pair<Integer,Integer> getAirplanePos(String line){
        //Split line by "," separator
        String [] receiveData = line.split(",");
        int x=Integer.parseInt(receiveData[receiveData.length-1]);//Lat
        int y=Integer.parseInt(receiveData[receiveData.length-2]);//Long
        return new Pair<>(x,y);
    }
}
