package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.util.Pair;
import servers.MyInterpreter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;


public class Model extends Observable {

    //CalcPath server variables
    Socket socketCalcServer;
    PrintWriter outClientCalcServer;
    BufferedReader inClientCalcServer;

    //Interpreter server variables
    Socket socketInterpreter;
    PrintWriter outInterpreter;
    BufferedReader inInterpreter;
    private volatile boolean stopClientInterpreter = false;
    private volatile boolean stopServerInterpreter = false;
    double airplaneX;
    double airplaneY;
    //Represent the row and col in MapDisplayer.mapData

    public DoubleProperty scale;

    public Model() {
        scale = new SimpleDoubleProperty();
        startServerInterpreter(5404);
    }

    public void sendJoystickValToSim(double joystickValX, double joystickValY) {
        //Convert joystick values to FlightSimulator valid values then send it
        double aileron = (joystickValX - 651) / 80; //left-right -1 to 1 ~~~ 731 = 1 ,571 = -1
        double elevator = (joystickValY - 196) / 80; //up-down -1 to 1 ~~~ 276 = 1 ,116 = -1
        String[] move = {
                "aileron = " + aileron,
                "elevator = " + elevator
        };
        sentToInterpreterServer(move);
    }

    public void rudderChange(double rudderVal) {
        String[] move = {
                "rudder = " + rudderVal
        };
        sentToInterpreterServer(move);
    }

    public void throttleChange(double throttleVal) {
        String[] move = {
                "throttle = " + throttleVal
        };
        sentToInterpreterServer(move);
    }

    public void connectToInterpreterServer(String ip, int port) {
        //connect the GUI to MyInterpreter server
        startClientInterpreter(ip, port); // TODO:Need to check when it finish
        String[] initial = {
                "var aileron",
                "var elevator",
                "var rudder",
                "var throttle",
                "var breaks",
                "var heading",
                "var alt",
                "var pitch",
                "var roll",

                "roll = bind simRoll",
                "breaks = bind simBreaks",
                "heading = bind simHeading",
                "alt = bind simAltitude",
                "pitch = bind simPitch",
                "aileron = bind simAileron",
                "elevator = bind simElevator",
                "rudder = bind simRudder",
                "throttle = bind simThrottle"
        };
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sentToInterpreterServer(initial);
    }

    public void connectToCalcServer(String ip, int port, int[][] matrix, double targetX, double targetY) throws IOException {
        try {
            //Check if we already created connection once
            if (this.socketCalcServer == null) {
                socketCalcServer = new Socket(ip, port);
                this.outClientCalcServer = new PrintWriter(socketCalcServer.getOutputStream());
                this.inClientCalcServer = new BufferedReader(new InputStreamReader(socketCalcServer.getInputStream()));
            }

            //Send the matrix
            for (int[] ints : matrix) {
                StringBuilder line = new StringBuilder();
                for (int anInt : ints) {
                    line.append(anInt).append(",");
                }
                line.deleteCharAt(line.length() - 1);
                outClientCalcServer.println(line);
                outClientCalcServer.flush();
            }
            outClientCalcServer.println("end");

            //Send the airplane position
//            Pair<Integer, Integer> airplanePositions = calcPositions(airplaneX, airplaneY);
//            outClientCalcServer.println(airplanePositions.getKey()+","+airplanePositions.getValue());
            outClientCalcServer.println(0 + "," + 0);

//            Send the target position
//            outClientCalcServer.println(3+","+3);
            outClientCalcServer.println((int) targetY + "," + (int) targetX);

            outClientCalcServer.flush();

            CompletableFuture.supplyAsync(() -> {
                //Wait for response
                String response;
                while (true) {
                    try {
                        if (!((response = inClientCalcServer.readLine()) == null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return response;
            }).thenAccept(path -> {
                //Once the response (path) is returned, notify the ViewModel
                setChanged();
                notifyObservers(path);
                outClientCalcServer.close();
                try {
                    inClientCalcServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    this.inClientCalcServer.close();
                    this.socketCalcServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.outClientCalcServer.close();
            });


        } catch (IOException e) {
            System.out.println("Failed!!!");
        }
    }

    private Pair<Integer, Integer> calcPositions(double x, double y) {
        int lng, lat;
        lat = (int) (Math.abs(Math.abs(x) - 21.443738) * scale.get());
        lng = (int) (Math.abs(Math.abs(y) - 158.020959) * scale.get());
        Pair<Integer, Integer> positions = new Pair<>(lng, lat);
        return positions;
    }

    public void startClientInterpreter(String ip, int port) { // start client to connect to MyInterpreter server
        new Thread(() -> runClientInterpreter(ip, port)).start();
    }

    private void runClientInterpreter(String ip, int port) {
        while (!stopClientInterpreter) {
            try {
                socketInterpreter = new Socket(ip, port);
                outInterpreter = new PrintWriter(socketInterpreter.getOutputStream());
                while (!stopClientInterpreter) {

                }
//                outInterpreter.close();
//                socketInterpreter.close();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    public void closeClientInterpreterServer() {
        stopClientInterpreter = true;
    }

    public void startServerInterpreter(int port) { // start client to connect to MyInterpreter server
        new Thread(() -> runServerInterpreter(port)).start();
    }

    private void runServerInterpreter(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            System.out.print("Gui server Thread: Waiting for interpreter\n");
            while (!stopServerInterpreter) {
                try {
                    System.out.print(".");
                    Socket socket = server.accept();
                    System.out.println("Gui server Thread: interpreter Connected");
                    inInterpreter = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    System.out.println("Before while");
                        while (!(line = inInterpreter.readLine()).equals("bye")) {
                            System.out.println("Gui server received: " + line);
                        }
                    System.out.println("After while");
                    System.out.println("MyInterpreter server: Client DisConnected");
                    inInterpreter.close();
                    socket.close();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
            }
            server.close();
        } catch (IOException e) {
        }

    }

    public synchronized void sentToInterpreterServer(String[] lines) { // send data to MyInterpreter server
        if (outInterpreter == null)
            return;

        for (String line : lines) {
            outInterpreter.println(line);
            outInterpreter.flush();
        }
    }
}

