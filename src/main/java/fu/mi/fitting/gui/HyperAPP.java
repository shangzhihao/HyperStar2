/**
 * Created by shang on 4/29/2016.
 * Application starts here.
 */

package fu.mi.fitting.gui;

import fu.mi.fitting.controller.GuiResource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class HyperAPP extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GuiResource.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("HyperStar2");
        primaryStage.setScene(new Scene(root, 1027, 500));
        primaryStage.show();
    }
}
