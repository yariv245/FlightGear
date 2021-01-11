package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import viewModel.ViewModelSimulator;

import java.io.IOException;

public class SimulatorController {
    //references for fxml

    ViewModelSimulator viewModelSimulator;
    @FXML
    MapDisplayer mapDisplayer;
    @FXML
    Circle joystick;
    @FXML
    Circle outer_bounds;
    @FXML
    TextField IP_textField;
    @FXML
    TextField port_textField;
    @FXML
    Button connectBtn;
    double maxRadius = 80;

    @FXML
    public void initialize() {
        initializeJoystick();
    }

    public void initializeJoystick(){
        joystick.setOnMouseDragged(mouseEvent -> {
            Point2D centerPoint = new Point2D(651, 196);
            Point2D mouse = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            double dis = centerPoint.distance(mouse);
            if (dis > maxRadius) { // if joystick get out of bounds
                double angle = Math.atan2(mouse.getY() - centerPoint.getY(), mouse.getX() - centerPoint.getX()); // cal angle between 2 points
                // force joystick to stay on his bounds
                joystick.setLayoutX(centerPoint.getX() + maxRadius * Math.cos(angle));
                joystick.setLayoutY(centerPoint.getY() + maxRadius * Math.sin(angle));
            } else { // in bounds
                joystick.setLayoutX(mouseEvent.getSceneX());
                joystick.setLayoutY(mouseEvent.getSceneY());
            }
        });
        joystick.setOnMouseReleased(mouseDragEvent -> {
            joystick.setLayoutX(651);
            joystick.setLayoutY(196);
        });
    }
    public void setViewModelSimulator(ViewModelSimulator vm) {
        this.viewModelSimulator = vm;
    }

    public void client_connect() {
        connect_popup(); // function to open the connect popup
        viewModelSimulator.server_ip.bind(IP_textField.textProperty());
        viewModelSimulator.server_port.bind(port_textField.textProperty());
        connectBtn.setOnAction(actionEvent -> { // set handler to connect button for client connection
            try {
                viewModelSimulator.client_connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void calc_path() {
        connect_popup();
        viewModelSimulator.server_ip.bind(IP_textField.textProperty());
        viewModelSimulator.server_port.bind(port_textField.textProperty());
        connectBtn.setOnAction(actionEvent -> { // set handler to connect button for server connection
            try {
                viewModelSimulator.calc_path();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void load_data() throws IOException {
        viewModelSimulator.load_data(mapDisplayer);
    }

    public void connect_popup(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./view/connect_view.fxml"));
        loader.setController(this);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Connect");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
