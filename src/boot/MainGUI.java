package boot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import servers.Simulator;
import view.SimulatorController;
import viewModel.ViewModelSimulator;

public class MainGUI extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Simulator.startServer(5400); // start Simulator server

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/simulator.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Flight simulator gear");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Model model = new Model();
        ViewModelSimulator viewModelSimulator = new ViewModelSimulator(model);
        SimulatorController simulatorController = loader.getController();
        simulatorController.setViewModelSimulator(viewModelSimulator);

    }


    public static void main(String[] args) {
        launch(args);
    }
}

