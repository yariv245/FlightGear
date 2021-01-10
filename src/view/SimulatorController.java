package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
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
    double maxRadius = 80 ;

    @FXML
    public void initialize() {
        joystick.setOnMouseDragged(mouseEvent -> {
            joystick.setLayoutX(mouseEvent.getSceneX());
            joystick.setLayoutY(mouseEvent.getSceneY());
        });
        joystick.setOnMouseReleased(mouseDragEvent -> {
            joystick.setLayoutX(651);
            joystick.setLayoutY(196);
        });
//        outer_bounds.setOnMouseExited(mouseEvent -> {
//
//            System.out.println("out of circle bounds");
//        });

        joystick.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {
                Point2D centerPoint = new Point2D(651,196);

                double dis = centerPoint.distance(mouseEvent.getSceneX(),mouseEvent.getSceneY());
                if(dis >maxRadius){
//                    joystick.setLayoutX();
//                    joystick.setLayoutY();
                }
            }
        });

    }

    public void setViewModelSimulator(ViewModelSimulator vm) {
        this.viewModelSimulator = vm;

    }


    public void open_window() throws IOException {
        viewModelSimulator.open_window();
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
