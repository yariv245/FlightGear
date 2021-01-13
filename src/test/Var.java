package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import commands.ConnectCommand;

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
                // if the current Var bound to Var
                if (this.getName().isEmpty()) { // check if the name parm in Var is empty
                    String msg = "set " + this.getName() + " " + this.getValue(); // prepare msg "set ...... 5"
                    PrintWriter out = null;
                    MyInterpreter.sentToServer(msg); // send to server the message to change the parameter
//                    try {
//                        while (ConnectCommand.client == null || ConnectCommand.client.isClosed()) {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                        out = new PrintWriter(ConnectCommand.client.getOutputStream());
//                        out.println(msg);
//                        out.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                ;
            }
        }

    }
}
