package model;

import calcServer.MyClientHandler;
import calcServer.MySerialServer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;
import servers.Simulator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;


public class Model extends Observable {

    //Here we have to implement the calculations and functions
    public DoubleProperty scale = new SimpleDoubleProperty();

    public void sendJoystickValToSim(double joystickValX, double joystickValY) {
        //Convert joystick values to FlightSimulator valid values
        double aileron = (joystickValX - 651) / 80; //left-right -1 to 1 ~~~ 731 = 1 ,571 = -1
        double elevator = (joystickValY - 196) / 80; //up-down -1 to 1 ~~~ 276 = 1 ,116 = -1
        String[] move = {
                "aileron = " + aileron,
                "elevator = " + elevator
        };
        Simulator.sentToServer(move);
    }

    public void rudderChange(double rudderVal) {
        String[] move = {
                "rudder = " + rudderVal
        };
        Simulator.sentToServer(move);
    }

    public void throttleChange(double throttleVal) {
        String[] move = {
                "throttle = " + throttleVal
        };
        Simulator.sentToServer(move);
    }

    public void connectToServer(String ip, int port) {

        Simulator.startClient(ip, port); // TODO:Need to check when it finish
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
        Simulator.sentToServer(initial);
    }

    public void connectToCalcServer(String ip, int port, int[][] matrix, double targetX, double targetY,double airplaneX, double airplaneY) throws IOException {
        try {
            Socket socket = new Socket(ip, port);
            PrintWriter outClient = new PrintWriter(socket.getOutputStream());

            //Send the matrix
            for (int i = 0; i < matrix.length; i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < matrix[i].length; j++) {
                    line.append(matrix[i][j] + ",");
                }
                line.deleteCharAt(line.length() - 1);
                outClient.println(line);
                outClient.flush();
            }
            outClient.println("end");

            //Send the airplane position
            Pair<Integer,Integer> airplanePositions = calcPositions(airplaneX,airplaneY);
//            outClient.println(airplanePositions.getKey()+","+airplanePositions.getValue());
            outClient.println(0+","+0);
//            Send the target position
            outClient.println((int)targetY+","+(int)targetX);

            outClient.flush();

            //Wait for response
            //Do something
            outClient.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed!!!");
        }
    }

    private Pair<Integer, Integer> calcPositions(double x, double y) {
        int lng, lat;
        lng = (int) (Math.abs(Math.abs(x) - 21.443738) * scale.get());
        lat = (int) (Math.abs(Math.abs(y) - 158.020959) * scale.get());
        Pair<Integer, Integer> positions = new Pair<>(lng, lat);
        return positions;
    }

}

