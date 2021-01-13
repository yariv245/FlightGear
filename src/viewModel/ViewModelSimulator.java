package viewModel;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import test.Simulator;
import view.MapDisplayer;

import java.io.*;
import java.util.*;

public class ViewModelSimulator extends Observable implements Observer{

    Model model;
    //functions and data

    public StringProperty server_ip;
    public StringProperty server_port;

    public DoubleProperty joystickValX = new SimpleDoubleProperty();
    public DoubleProperty joystickValY = new SimpleDoubleProperty();

    public DoubleProperty rudderVal = new SimpleDoubleProperty();
    public DoubleProperty throttleVal = new SimpleDoubleProperty();

    public ViewModelSimulator(Model model){
        this.model = model;
        server_ip = new SimpleStringProperty();
        server_port = new SimpleStringProperty();
    }

    public void client_connect() throws IOException {
        //do the actual connection by connect command
        String ip = server_ip.getValue();
        int port = Integer.parseInt(server_port.getValue());
        model.connectToServer(ip,port);
    }
    public void calc_path() throws IOException {
        System.out.println("Try to server connect");
    }

    public void joystickMovement(){
        model.sendJoystickValToSim(this.joystickValX.get(),this.joystickValY.get());
    }

    public void rudderChange(){
        model.rudderChange(this.rudderVal.get());
    }

    public void throttleChange(){
        model.throttleChange(this.throttleVal.get());
    }

    public void load_data(MapDisplayer mapDisplayer) throws IOException {
        List<String[]> r = null;
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        String pathFile = null;
        if (file != null)
            pathFile = file.getPath();

        try (CSVReader reader = new CSVReader(new FileReader(pathFile))) {
            r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
        } catch (CsvException e) {
            e.printStackTrace();
        }

        int[][] csvAsInt = new int[r.size() - 2][];

        for (int i = 2; i < r.size(); i++) {
            csvAsInt[i - 2] = new int[r.get(i).length];

            for (int j = 0; j < r.get(i).length; j++)
                csvAsInt[i - 2][j] = Integer.parseInt(r.get(i)[j]);
        }

        for (int i = 0; i < csvAsInt.length; i++)
            Collections.reverse(Arrays.asList(csvAsInt[i]));

        mapDisplayer.setOnMouseClicked(arg0 -> {
            System.out.println("The X on the matrix is : " + arg0.getX() / 2);
            System.out.println("The Y on the matrix is : " + arg0.getY() / 2);
            mapDisplayer.gc.strokeText("X", arg0.getX(), arg0.getY());
        });
        mapDisplayer.setMapData(csvAsInt);

    }

    @Override
    public void update(Observable o, Object arg) {
        //Notify
    }
}
