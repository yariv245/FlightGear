package viewModel;

import commands.ConnectCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class ViewModelConnect {

    //data and logic

    public StringProperty ip;
    public StringProperty port;


    public ViewModelConnect() {
        ip = new SimpleStringProperty();
        port = new SimpleStringProperty();
    }

    public void connect_server() {
        //do the actual connection by connect command
        ConnectCommand connectCommand = new ConnectCommand();
        ArrayList<String> data = new ArrayList<>();
        data.add(ip.getValue());//ip
        data.add(port.getValue());//port
        connectCommand.doCommand(data);
    }

}
