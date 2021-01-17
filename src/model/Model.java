package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;


public class Model extends Observable {
    //Here we have to implement the calculations and functions

    public DoubleProperty scale = new SimpleDoubleProperty();
    PrintWriter outClientCalcServer = null;
    BufferedReader inClientCalcServer = null;
    Socket socketCalcPath = null;
    public DoubleProperty airplaneX;
    public DoubleProperty airplaneY;
    PrintWriter outClientInterpreterServer = null;
    Socket socketGuiServer;
    BufferedReader inFromGuiServer;

    private volatile boolean stopClientInterpreterServer;

    public Model() {
        socketGuiServer= new Socket();
//        inFromGuiServer = new BufferedReader();
        airplaneX = new SimpleDoubleProperty();
        airplaneY = new SimpleDoubleProperty();
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
        startClientInterpreterServer(ip, port); // TODO:Need to check when it finish
        String[] initial = {
                "var aileron",
                "var elevator",
                "var rudder",
                "var throttle",
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
            if (this.socketCalcPath == null) {
                socketCalcPath = new Socket(ip, port);
                this.outClientCalcServer = new PrintWriter(socketCalcPath.getOutputStream());
                this.inClientCalcServer = new BufferedReader(new InputStreamReader(socketCalcPath.getInputStream()));
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
                    this.socketCalcPath.close();
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

    public void startClientInterpreterServer(String ip, int port) { // start client to connect to MyInterpreter server
        new Thread(() -> runClientInterpreterServer(ip, port)).start();
    }

    private void runClientInterpreterServer(String ip, int port) {
        while (!stopClientInterpreterServer) {
            try {
                Socket interpreter = new Socket(ip, port);
                outClientInterpreterServer = new PrintWriter(interpreter.getOutputStream());
                while (!stopClientInterpreterServer) {
//                    out.println(simX + "," + simY + "," + simZ);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e1) {
//                    }
                }
                outClientInterpreterServer.close();
                interpreter.close();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    public void closeClientInterpreterServer() {
        stopClientInterpreterServer = true;
    } // close client connection to MyInterpreter server

    public void sentToInterpreterServer(String[] lines) { // send data to MyInterpreter server
        if (outClientInterpreterServer == null)
            return;

        for (String line : lines) {
            outClientInterpreterServer.println(line);
            outClientInterpreterServer.flush();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}

