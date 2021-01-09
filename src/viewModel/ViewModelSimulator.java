package viewModel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.ConnectController;

import java.io.File;
import java.io.IOException;

public class ViewModelSimulator {

    //functions and data

    public void open_window() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./view/connect_view.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Connect server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        ViewModelConnect viewModelConnect = new ViewModelConnect();
        ConnectController connectController = loader.getController();
        connectController.setConnectController(viewModelConnect);
    }

    public void load_data() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        String pathFile = null;
        if (file != null)
            pathFile = file.getPath();
        System.out.println(pathFile);
    }

}
