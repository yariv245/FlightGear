package model;

import javafx.beans.property.DoubleProperty;
import test.MyInterpreter;
import test.Simulator;

import java.util.Observable;


public class Model extends Observable {

    //Here we have to implement the calculations and functions

    public void sendJoystickValToSim(double joystickValX, double joystickValY) {
        //Convert joystick values to FlightSimulator valid values
        double aileron = (joystickValX - 651) / 80; //left-right -1 to 1 ~~~ 731 = 1 ,571 = -1
        double elevator = (joystickValY - 196) / 80; //up-down -1 to 1 ~~~ 276 = 1 ,116 = -1
        String[] move = {
                "aileron = " + aileron ,
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
        Simulator.startClient(ip,port); // TODO:Need to check when it finish
        String[] line = {
                "var aileron",
                "var elevator",
                "aileron = bind simAileron",
                "elevator = bind simElevator",
        };
        Simulator.sentToServer(line); // TODO : send the array to fast!
    }
}
