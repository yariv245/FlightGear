package viewModel;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import commands.ConnectCommand;
import javafx.application.HostServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.ConnectController;
import view.MapDisplayer;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class ViewModelSimulator {

    //functions and data
    public StringProperty client_ip;
    public StringProperty client_port;

    public StringProperty server_ip;
    public StringProperty server_port;
    public ViewModelSimulator(){
        client_ip = new SimpleStringProperty();
        client_port = new SimpleStringProperty();
        server_ip = new SimpleStringProperty();
        server_port = new SimpleStringProperty();
    }

    public void client_connect() throws IOException {
        //do the actual connection by connect command
        ConnectCommand connectCommand = new ConnectCommand();
        ArrayList<String> data = new ArrayList<>();
        data.add(server_ip.getValue());//ip
        data.add(server_port.getValue());//port
        connectCommand.doCommand(data);
    }
    public void calc_path() throws IOException {
        System.out.println("Try to server connect");
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


}
