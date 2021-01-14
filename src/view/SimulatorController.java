package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import viewModel.ViewModelSimulator;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class SimulatorController implements Observer {
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
    @FXML
    Slider rudder_slider;
    @FXML
    Slider throttle_slider;

    double maxRadius = 80;
    DoubleProperty joystickValX = new SimpleDoubleProperty();
    DoubleProperty joystickValY = new SimpleDoubleProperty();

    @FXML
    public void initialize() {
        initializeJoystick();
        rudder_slider.valueProperty().addListener((observableValue, number, t1) -> rudderChange());
        throttle_slider.valueProperty().addListener((observableValue, number, t1) -> throttleChange());
    }

    public void initializeJoystick(){
        joystickValX.bind(joystick.layoutXProperty());
        joystickValY.bind(joystick.layoutYProperty());
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
            viewModelSimulator.joystickMovement();

        });



        joystick.setOnMouseReleased(mouseDragEvent -> {
            joystick.setLayoutX(651);
            joystick.setLayoutY(196);
        });
    }
    public void setViewModelSimulator(ViewModelSimulator vm) {
        viewModelSimulator = vm;
        viewModelSimulator.joystickValX.bind(this.joystickValX);
        viewModelSimulator.joystickValY.bind(this.joystickValY);
        viewModelSimulator.throttleVal.bind(throttle_slider.valueProperty());
        viewModelSimulator.rudderVal.bind(rudder_slider.valueProperty());
    }

    public void rudderChange(){
        viewModelSimulator.rudderChange();
    }

    public void throttleChange(){
        viewModelSimulator.throttleChange();
    }

    public void client_connect() {
        connect_popup(); // function to open the connect popup
        connectBtn.setOnAction(actionEvent -> { // set handler to connect button for client connection
            viewModelSimulator.client_connect();
        });
    }

    public void calc_path() {
        connect_popup();
        connectBtn.setOnAction(actionEvent -> { // set handler to connect button for server connection
            try {
                viewModelSimulator.calc_path(this.mapDisplayer.mapData);
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

        viewModelSimulator.server_ip.bind(IP_textField.textProperty());
        viewModelSimulator.server_port.bind(port_textField.textProperty());

        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
