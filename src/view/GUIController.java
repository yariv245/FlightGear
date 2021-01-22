package view;

import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;
import viewModel.ViewModelSimulator;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class GUIController implements Observer {

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
    Button calcPath_btn;
    @FXML
    Slider rudder_slider;
    @FXML
    Slider throttle_slider;
    @FXML
    ImageView airplane;

    double maxRadius = 80;
    DoubleProperty joystickValX = new SimpleDoubleProperty();
    DoubleProperty joystickValY = new SimpleDoubleProperty();

    public DoubleProperty airplaneX = new SimpleDoubleProperty();//Todo: bind this prop to realtime airplane pos
    public DoubleProperty airplaneY = new SimpleDoubleProperty();

    Stage primaryStage;

    @FXML
    public void initialize() {
        initializeJoystick();
        rudder_slider.valueProperty().addListener((observableValue, number, t1) -> rudderChange());
        throttle_slider.valueProperty().addListener((observableValue, number, t1) -> throttleChange());
    }

    public void initializeJoystick() {
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
            viewModelSimulator.joystickMovement();
        });
    }

    public void setViewModelSimulator(ViewModelSimulator vm) {
        viewModelSimulator = vm;
        viewModelSimulator.joystickValX.bind(this.joystickValX);
        viewModelSimulator.joystickValY.bind(this.joystickValY);
        viewModelSimulator.throttleVal.bind(throttle_slider.valueProperty());
        viewModelSimulator.rudderVal.bind(rudder_slider.valueProperty());
        this.viewModelSimulator.addObserver(this);

        this.airplaneX.bind(viewModelSimulator.airplaneX);
        this.airplaneY.bind(viewModelSimulator.airplaneY);
    }

    public void rudderChange() {
        viewModelSimulator.rudderChange();
    }

    public void throttleChange() {
        viewModelSimulator.throttleChange();
    }

    public void client_connect() {
        connect_popup(); // function to open the connect popup
        connectBtn.setOnAction(actionEvent -> { // set handler to connect button for client connection
            viewModelSimulator.client_connect();
            if (primaryStage != null)
                primaryStage.close();
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
            if (primaryStage != null)
                primaryStage.close();
            calcPath_btn.setDisable(true);
            calcPath_btn.setText("Connected");
        });
    }

    public void load_data() throws IOException {
        viewModelSimulator.load_data(mapDisplayer);
    }

    public void connect_popup() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./view/connect_view.fxml"));
        loader.setController(this);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage = new Stage();
        primaryStage.setTitle("Connect");
        primaryStage.setScene(new Scene(root));

        viewModelSimulator.server_ip.bind(IP_textField.textProperty());
        viewModelSimulator.server_port.bind(port_textField.textProperty());

        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.getClass().getName().equals("java.lang.String"))
            this.mapDisplayer.drawPath(arg.toString());
//        else{
        if (!airplane.isVisible())
            airplane.setVisible(true);
        //Convert the observable data to String[]
//            String[] data = (String[])arg;
        String[] data = (new String[]{"21.308333", "-157.930417", "90.0"});
        Pair<Double, Double> positions = calcPositions(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
        double x = positions.getKey()*-1;
        double y = positions.getValue();
        //Update the airplane position
        airplane.setLayoutX(x);
        airplane.setLayoutY(y);
        System.out.println("x: " + x);
        System.out.println("y: " + y);

        try {
            Thread.sleep(3000);
            airplane.setLayoutX(x+65);
            airplane.setLayoutY(y+ 8 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        airplane.setRotate(Double.parseDouble(data[2]));
//        }
    }

    private Pair<Double, Double> calcPositions(double realLat, double realLng) {
        double lng, lat;
//        lat = (realLat - 85) * 2.0588;
//        lng = (realLng + 180) * 2.22222222222;
        double x = ((realLng  * 800 / 180) + (800 / 2))/3.03030303030303030303;
        double y = (( realLat * 350 / 360.0) + (350 / 2))/1.4893617021276595;
        return new Pair<>(x, y);
    }



//    private Pair<Double, Double> calcPositions(double realLat, double realLng) {
//        double lng, lat;
//        lat = (realLat - 85) * 1.3823;
//        lng = (realLng + 180) * 0.7333333;
//        return new Pair<>(lng, lat);
//    }

    /*
    *     private void drawAirplane(GraphicsContext graphicsContext){
        graphicsContext.save();
        Rotate r = new Rotate(angle, airplane_pos.getX() * cube_length, airplane_pos.getY() * cube_length);
        graphicsContext.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        graphicsContext.drawImage(AirplaneImage, airplane_pos.getX() * cube_length, airplane_pos.getY() * cube_length);
        graphicsContext.restore();
    }
    *             double x = Double.parseDouble(input[0]);
            double y = Double.parseDouble(input[1]);
            angle = Double.parseDouble(input[2]);
            x = (x - startPos.getX() + offset) / offset;
            y = Math.abs((y - startPos.getY() + offset) / offset);
            airplane_pos.setX(x);
            airplane_pos.setY(y);
    * */
}
