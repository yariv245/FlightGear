package commands;

import java.util.Observable;
import java.util.Observer;

import servers.MyInterpreter;

public class Var extends Observable implements Observer {
    private double value;
    private String name;

    public Var(double value, String name) {
        this.value = value;
        this.name = name;
    }

    public Var(double value) {
        this.value = value;
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        this.setChanged();
        this.notifyObservers(value);
    }

    @Override
    public void update(Observable observable, Object arg) {
        Double d = new Double(0);
        if (arg.getClass() == (d.getClass())) {
            if (this.value != (double) arg) {
                this.setValue((double) arg);
                String msg;
                // if the current Var bound to Var
                if (!this.getName().isEmpty() && (this.getName().contains("/"))) { // check if the name parm in Var is empty
                    msg = "set " + this.getName() + " " + this.getValue(); // prepare msg "set ...... 5"
                    MyInterpreter.sendToClientFlightGear(msg); // send to server the message to change the parameter
                }else{
                    msg = "set " + this.getName() + " " + this.getValue(); // prepare msg "set ...... 5"
                    MyInterpreter.sendToClientGui(msg); // send to server the message to change the parameter
                }
            }
        }

    }
}
