package view;

import commands.ConnectCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.MapDisplayer;
import viewModel.ViewModelSimulator;

import java.io.IOException;
import java.util.ArrayList;

public class SimulatorController {
    //references for fxml

    ViewModelSimulator viewModelSimulator;
    @FXML
    MapDisplayer mapDisplayer;

    public void setSimulatorController(ViewModelSimulator vm){
//        mapDisplayer.mapData[0][0] =null;
        this.viewModelSimulator = vm;
//        for(int i = 0;i<mapDisplayer.mapData.length;i++)
//            for(int j = 0;j<mapDisplayer.mapData[i].length;j++)
//                mapDisplayer.mapData[i][j].bind();

    }


    public void open_window() throws IOException {
        viewModelSimulator.open_window();
    }

    public void load_data(ActionEvent actionEvent) {
        viewModelSimulator.load_data();
    }
}
