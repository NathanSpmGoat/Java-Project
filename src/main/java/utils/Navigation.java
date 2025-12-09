package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {

    public static void goTo(String fxmlPath, Node anyNodeInScene) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigation.class.getResource("/view/" + fxmlPath)
            );
            Parent root = loader.load();

            Stage stage = (Stage) anyNodeInScene.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
