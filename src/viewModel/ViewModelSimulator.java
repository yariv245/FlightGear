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
import view.MapDisplayer;

import java.io.*;
import java.util.*;

public class ViewModelSimulator extends Observable implements Observer {

    Model model;
    //functions and data

    public StringProperty server_ip;
    public StringProperty server_port;

    public DoubleProperty joystickValX = new SimpleDoubleProperty();
    public DoubleProperty joystickValY = new SimpleDoubleProperty();

    public DoubleProperty rudderVal = new SimpleDoubleProperty();
    public DoubleProperty throttleVal = new SimpleDoubleProperty();

    public DoubleProperty airplaneX = new SimpleDoubleProperty();//Todo: bind this prop to realtime airplane pos
    public DoubleProperty airplaneY = new SimpleDoubleProperty();

    public DoubleProperty targetX = new SimpleDoubleProperty();
    public DoubleProperty targetY = new SimpleDoubleProperty();

    public DoubleProperty scale = new SimpleDoubleProperty();

    private boolean firstTime = true;
    private boolean calculating = false;

    public ViewModelSimulator(Model model) {
        this.model = model;
        server_ip = new SimpleStringProperty();
        server_port = new SimpleStringProperty();
        model.scale.bindBidirectional(this.scale);
        this.model.addObserver(this);
    }

    public void client_connect() {
        //do the actual connection by connect command
        String ip = server_ip.getValue();
        int port = Integer.parseInt(server_port.getValue());
        model.connectToServer(ip, port);
    }

    public void calc_path(int[][] matrix) throws IOException {
        String ip = server_ip.getValue();
        int port = Integer.parseInt(server_port.getValue());
        model.connectToCalcServer(ip, port, matrix, targetX.get(), targetY.get(), airplaneX.get(), airplaneY.get());
    }

    public void joystickMovement() {
        model.sendJoystickValToSim(this.joystickValX.get(), this.joystickValY.get());
    }

    public void rudderChange() {
        model.rudderChange(this.rudderVal.get());
    }

    public void throttleChange() {
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
        } catch (CsvException e) {
            e.printStackTrace();
        }

        int[][] csvAsInt = new int[r.size() - 2][];

        airplaneX.setValue(Double.parseDouble(r.get(0)[0]));
        airplaneY.setValue(Double.parseDouble(r.get(0)[1]));

        scale.setValue(Double.parseDouble(r.get(1)[0]));
        System.out.println(r.size() + "," + r.get(5).length);

        for (int i = 2; i < r.size(); i++) {
            csvAsInt[i - 2] = new int[r.get(i).length];

            for (int j = 0; j < r.get(i).length; j++)
                csvAsInt[i - 2][j] = Integer.parseInt(r.get(i)[j]);
        }

//        for (int i = 0; i < csvAsInt.length; i++) TODO:remove this if not necessary
//            Collections.reverse(Arrays.asList(csvAsInt[i]));

        mapDisplayer.setOnMouseClicked(arg0 -> {
            if(!calculating) {
                targetX.setValue(arg0.getX() / 1.0688259109311740890688);
                targetY.setValue(arg0.getY() / 1.546052631578947368421);
                if (!firstTime) {
                    mapDisplayer.reDraw();
                    try {
                        this.calc_path(mapDisplayer.mapData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//            System.out.println("The X on the matrix is : " + arg0.getX() / 1.0688259109311740890688);
//            System.out.println("The Y on the matrix is : " + arg0.getY() / 1.546052631578947368421);
                mapDisplayer.gc.strokeText("X", arg0.getX(), arg0.getY());
                calculating= true;
            }

        });
        mapDisplayer.setMapData(csvAsInt);

    }

    @Override
    public void update(Observable o, Object arg) {
        //Notify just arrived
//        System.out.println(arg.toString());
        setChanged();
        notifyObservers(arg);
        this.firstTime = false;
        calculating = false;
    }
}
