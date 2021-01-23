package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
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
    RadioButton autoPilot_radio_btn;
    @FXML
    ImageView airplane;

    double maxRadius = 80;
    DoubleProperty joystickValX = new SimpleDoubleProperty();
    DoubleProperty joystickValY = new SimpleDoubleProperty();

    public DoubleProperty airplaneX = new SimpleDoubleProperty();//Todo: bind this prop to realtime airplane pos
    public DoubleProperty airplaneY = new SimpleDoubleProperty();

    Stage primaryStage;

    Boolean afterTakeOff = false;

    @FXML
    public void initialize() {
        initializeJoystick();
        rudder_slider.valueProperty().addListener((observableValue, number, t1) -> rudderChange());
        throttle_slider.valueProperty().addListener((observableValue, number, t1) -> throttleChange());
        autoPilot_radio_btn.setOnAction(actionEvent -> {
            if (!afterTakeOff)
                takeOff();
            //Todo: navigate to target
        });
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

    public void takeOff() {
        String[] takeOffCommands1 = {
                "breaks = 0",
                "throttle = 1",
                "var h = heading",
                "var minus = -1",
                "var a = alt",
                "print start",
                "while a - alt > -50 {",
                "rudder = ( h - heading ) / 200",
                "aileron = minus * roll / 200",
                "print aileron",
                "elevator = pitch / 50",
                "print elevator",
                "print alt",
                "}",
                "print here3",
                "print change",
                "while alt < 1000 {",
                "rudder = ( h - heading ) / 200",
                "print here",
                "aileron = minus * roll / 200",
                "elevator = pitch / 50",
                "print alt",
                "}",
                "print done",
        };

        String[] takeOffCommands = {
                "breaks = 0",
                "throttle = 1",
                "var h = heading",
                "while alt < 1000 {",
                "rudder = (h – heading) / 20",
                "aileron = 0",
                "elevator = 1",
                "print alt",
                "}"
        };

        this.viewModelSimulator.sentToInterpreterServer(takeOffCommands);
        this.afterTakeOff = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.getClass().getName().equals("java.lang.String"))
            this.mapDisplayer.drawPath(arg.toString());
//        else {
            if (!airplane.isVisible())
                airplane.setVisible(true);
            //Convert the observable data to String[]
//            String[] data = (String[])arg;
        String[] data = (new String[]{"21.315213", "-157.926505", "90.0"});
//            String[] data = (new String[]{"21.328867785399552", "-157.65725163345363", "90.0"});
            //
            Pair<Double, Double> positions = latlngToScreenXY(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
            double x = positions.getKey();
            double y = positions.getValue();
            //Update the airplane position
            airplane.setLayoutX(x);
            airplane.setLayoutY(y);

//            try {
//                Thread.sleep(3000);
//                String[] data2 = (new String[]{"21.238137", "-157.634102", "90.0"});
//                Pair<Double, Double> positions2 = latlngToScreenXY(Double.parseDouble(data2[0]), Double.parseDouble(data2[1]));
//                double x2 = positions2.getKey();
//                double y2 = positions2.getValue();
//                //Update the airplane position
//            airplane.setLayoutX(x2);
//            airplane.setLayoutY(y2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            airplane.setRotate(Double.parseDouble(data[2]));
//        }
    }

//    private Pair<Double, Double> calcPositions(double realLat, double realLng) {
//        double lng, lat;
////        lat = (realLat - 85) * 2.0588;
////        lng = (realLng + 180) * 2.22222222222;
//        double x = ((realLng  * 800 / 180) + (800 / 2))/3.03030303030303030303;
//        double y = (( realLat * 350 / 360.0) + (350 / 2))/1.4893617021276595;
//        return new Pair<>(x, y);
//    }

    double radius = 6371;

    public class referencePoint {
        public double srcX;
        double scrY;
        double lat;
        double lng;

        public referencePoint(double scrX, double scrY, double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
            this.srcX = scrX;
            this.scrY = scrY;
        }

    }

    //original
//    // Calculate global X and Y for top-left reference point
//    referencePoint p0 = new referencePoint(-4, 50, 21.443738, -158.020959);
//    // Calculate global X and Y for bottom-right reference point
//    referencePoint p1 = new referencePoint(260, 285, 21.238137136691147, -157.63410286953055);

    //1:1 map data
//    // Calculate global X and Y for top-left reference point
//    referencePoint p0  = new referencePoint(-4,50,21.443738,-158.020959);
//    // Calculate global X and Y for bottom-right reference point
//    referencePoint p1  = new referencePoint(243,202,21.238137136691147,-157.63410286953055);

    //tests
    // Calculate global X and Y for top-left reference point
    referencePoint p0 = new referencePoint(-4, 50, 21.443738, -158.020959);
    // Calculate global X and Y for bottom-right reference point
    referencePoint p1 = new referencePoint(260, 285, 21.15736059993721, -157.66010286953055);

    // This function converts lat and lng coordinates to GLOBAL X and Y positions
    public Pair<Double, Double> latlngToGlobalXY(double lat, double lng) {
        // Calculates x based on cos of average of the latitudes
        double x = radius * lng * Math.cos((p0.lat + p1.lat) / 2);
        //Calculates y based on latitude
        double y = radius * lat;
        return new Pair(x, y);
    }

    Pair<Double, Double> p0_pos = latlngToGlobalXY(p0.lat, p0.lng);
    Pair<Double, Double> p1_pos = latlngToGlobalXY(p1.lat, p1.lng);


    // This function converts lat and lng coordinates to SCREEN X and Y positions
    public Pair<Double, Double> latlngToScreenXY(double lat, double lng) {
        // Calculate global X and Y for projection point
        Pair<Double, Double> pos = latlngToGlobalXY(lat, lng);
        // Calculate the percentage of Global X position in relation to total global width
        double perX = ((pos.getKey() - p0_pos.getKey()) / (p1_pos.getKey() - p0_pos.getKey()));
        // Calculate the percentage of Global Y position in relation to total global height
        double perY = ((pos.getValue() - p0_pos.getValue()) / (p1_pos.getValue() - p0_pos.getValue()));

        // Returns the screen position based on reference points
        double returnX = p0.srcX + (p1.srcX - p0.srcX) * perX;
        double returnY = p0.scrY + (p1.scrY - p0.scrY) * perY;

        return new Pair<>(returnX, returnY);
    }

}
