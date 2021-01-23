package viewModel;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import view.MapDisplayer;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class ViewModelSimulator extends Observable implements Observer {

    Model model;

    public StringProperty server_ip;
    public StringProperty server_port;

    public DoubleProperty joystickValX = new SimpleDoubleProperty();
    public DoubleProperty joystickValY = new SimpleDoubleProperty();

    public DoubleProperty rudderVal = new SimpleDoubleProperty();
    public DoubleProperty throttleVal = new SimpleDoubleProperty();

    public DoubleProperty airplaneX = new SimpleDoubleProperty();
    public DoubleProperty airplaneY = new SimpleDoubleProperty();

    public DoubleProperty targetX = new SimpleDoubleProperty();
    public DoubleProperty targetY = new SimpleDoubleProperty();

    public DoubleProperty scale = new SimpleDoubleProperty();

    private boolean firstTime = true;
    private boolean calculating = false;
    private FileChooser fileChooser;


    public ViewModelSimulator(Model model) {
        this.model = model;
        server_ip = new SimpleStringProperty();
        server_port = new SimpleStringProperty();
        model.scale.bindBidirectional(this.scale);
        this.model.addObserver(this);
        fileChooser = new FileChooser();
    }

    public void client_connect() {
        //connect the GUI to MyInterpreter server
        String ip = server_ip.getValue();
        if (ip.equals("localhost"))
            ip = "127.0.0.1";
        int port = Integer.parseInt(server_port.getValue());
        model.connectToInterpreterServer(ip, port);
    }

    public void calc_path(int[][] matrix) throws IOException {
        //connect the GUI to calcPath server
        String ip = server_ip.getValue();
        if (ip.equals("localhost"))
            ip = "127.0.0.1";
        int port = Integer.parseInt(server_port.getValue());
        model.connectToCalcServer(ip, port, matrix, targetX.get(), targetY.get(), airplaneX.doubleValue() / 1.0688259109311740890688, airplaneY.getValue() / 1.546052631578947368421);
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

    public void load_text(TextArea text_area) {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath + "/src/resources"));
        File file = fileChooser.showOpenDialog(new Stage());
        String pathFile = null;
        if (file != null)
            pathFile = file.getPath();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(pathFile)))) {
            String line;
            while ((line = reader.readLine()) != null)
                text_area.appendText(line + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load_data(MapDisplayer mapDisplayer) throws IOException {
        List<String[]> csvMapData = null;
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath + "/src/resources"));
        File file = fileChooser.showOpenDialog(new Stage());
        String pathFile = null;
        if (file != null)
            pathFile = file.getPath();

        try (CSVReader reader = new CSVReader(new FileReader(pathFile))) {
            csvMapData = reader.readAll();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        int[][] csvAsInt = new int[csvMapData.size() - 2][];

        scale.setValue(Double.parseDouble(csvMapData.get(1)[0]));

        for (int i = 2; i < csvMapData.size(); i++) {
            csvAsInt[i - 2] = new int[csvMapData.get(i).length];

            for (int j = 0; j < csvMapData.get(i).length; j++)
                csvAsInt[i - 2][j] = Integer.parseInt(csvMapData.get(i)[j]);
        }

        mapDisplayer.setOnMouseClicked(arg0 -> {
            if (!calculating) {
                // place in matrix
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

                mapDisplayer.gc.strokeText("X", arg0.getX(), arg0.getY());
                calculating = true;
            }

        });
        mapDisplayer.setMapData(csvAsInt);
    }

    public void sentToInterpreterServer(String[] lines) {
        this.model.sentToInterpreterServer(lines);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
        this.firstTime = false;
        calculating = false;
    }
}
