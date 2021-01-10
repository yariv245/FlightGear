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
    public StringProperty ip;
    public StringProperty port;

    public ViewModelSimulator(){
        ip = new SimpleStringProperty();
        port = new SimpleStringProperty();
    }

    public void open_window() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./view/connect_view.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Connect server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        ViewModelConnect viewModelConnect = new ViewModelConnect();
        ConnectController connectController = loader.getController();
        connectController.setConnectController(viewModelConnect);
    }

    public void client_connect() throws IOException {
        //do the actual connection by connect command
        ConnectCommand connectCommand = new ConnectCommand();
        ArrayList<String> data = new ArrayList<>();
        data.add(ip.getValue());//ip
        data.add(port.getValue());//port
        connectCommand.doCommand(data);
    }
    public void calc_path() throws IOException {
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

        mapDisplayer.setMapData(csvAsInt);

    }


}
