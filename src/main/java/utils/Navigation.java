package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class Navigation {

    /**
     * Change la scène complète.
     */
    public static void goTo(String fxmlPath, Node anyNodeInScene) {
        try {
            URL fxmlUrl = Navigation.class.getResource("/view/" + fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("FXML non trouvé : " + fxmlPath);
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) anyNodeInScene.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre une fenêtre modale simple (pas de données transmises).
     */
    public static void openModal(String fxmlPath, String title) {
        try {
            URL fxmlUrl = Navigation.class.getResource("/view/" + fxmlPath);
            if (fxmlUrl == null) {
                System.out.println("FXML non trouvé : " + fxmlPath);
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre une fenêtre modale ET transmet des données au controller.
     *
     * @param fxmlPath  le FXML à charger
     * @param title     titre de la fenêtre
     * @param initController fonction lambda : controller -> assignation de données
     */
    public static <T> void openModalWithData(String fxmlPath, String title, Consumer<T> initController) {
        try {
            URL fxmlUrl = Navigation.class.getResource("/view/" + fxmlPath);
            if (fxmlUrl == null) {
                System.out.println("FXML non trouvé : " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // On récupère le controller et on lui injecte des données
            T controller = loader.getController();
            initController.accept(controller);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
