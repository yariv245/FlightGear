package view;

import commands.ConnectCommand;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import viewModel.ViewModelConnect;

import java.util.ArrayList;

public class ConnectController {
    //references for fxml and functions which call VM funcs

    ViewModelConnect viewModel;
    @FXML
    TextField IP_textField;
    @FXML
    TextField port_textField;

    public void setConnectController(ViewModelConnect vm) {
        this.viewModel = vm;
        viewModel.ip.bind(IP_textField.textProperty());
        viewModel.port.bind(port_textField.textProperty());
    }

    public void connect_server() {
        viewModel.connect_server();
    }

}
