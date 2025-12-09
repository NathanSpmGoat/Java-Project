package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/view/login.fxml")
        );

        Scene scene = new Scene(root);
        primaryStage.setTitle("Connexion");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}