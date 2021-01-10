package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import viewModel.ViewModelSimulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class SimulatorController {
    //references for fxml

    ViewModelSimulator viewModelSimulator;
    @FXML
    MapDisplayer mapDisplayer;
    @FXML
    Circle joystick;
    @FXML
    Circle outer_bounds;
    double maxRadius = 80;

    @FXML
    public void initialize() {
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
//        outer_bounds.setOnMouseExited(mouseEvent -> {
//
//            System.out.println("out of circle bounds");
//        });

//        joystick.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                joystick.setLayoutX(651);
//                joystick.setLayoutY(196);
//                Point2D centerPoint = new Point2D(651, 196);
//                Point2D mouse = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
//                double dis = centerPoint.distance(mouse);
//                if (dis > maxRadius) {
//
////                    joystick.setLayoutX();
////                    joystick.setLayoutY();
//                }
//            }
//        });

    }

    public void setViewModelSimulator(ViewModelSimulator vm) {
        this.viewModelSimulator = vm;

    }

    public void client_connect() throws IOException {
//        viewModelSimulator.open_window();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./view/connect_view.fxml"));
        loader.setController(this);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Connect server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    public void calc_path() throws IOException {
//        viewModelSimulator.open_window();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./view/connect_view.fxml"));
        loader.setController(this);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Connect server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void load_data() throws IOException {
        viewModelSimulator.load_data(mapDisplayer);

    }

//    @FXML public void onDragging(MouseEvent mouseEvent) {
//        System.out.println(mouseEvent.getX());
//        joystick.setLayoutX(mouseEvent.getX());
//        joystick.setLayoutY(mouseEvent.getY());
//    }
}
