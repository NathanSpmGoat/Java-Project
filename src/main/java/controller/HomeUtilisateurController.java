package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import model.entities.Vehicule;
import model.services.VehiculeService;

import java.io.IOException;
import java.sql.SQLException;

public class HomeUtilisateurController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrix;
    @FXML private TableColumn<Vehicule, String> colEtat;
    @FXML private TableColumn<Vehicule, Void> colAction;

    private final VehiculeService vehiculeService = new VehiculeService();

    @FXML
    public void initialize() throws SQLException {
        // Configurer les colonnes
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Ajouter bouton "Réserver"
        addReservationButton();

        // Remplir le tableau avec les véhicules disponibles uniquement
        tableVehicules.setItems(FXCollections.observableArrayList(
                vehiculeService.getAvailableVehicules()
        ));
    }

    private void addReservationButton() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Réserver");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                btn.setOnAction(e -> {
                    Vehicule selected = getTableView().getItems().get(getIndex());
                    openReservationDialog(selected);
                });
                setGraphic(btn);
            }
        });
    }

    private void openReservationDialog(Vehicule vehicule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reservation_form.fxml"));
            Scene scene = new Scene(loader.load());

            // Passer le véhicule sélectionné au controller
            ReservationFormController controller = loader.getController();
            controller.setVehicule(vehicule);

            Stage stage = new Stage();
            stage.setTitle("Réserver : " + vehicule.getMarque() + " " + vehicule.getModele());
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir le formulaire de réservation.");
            alert.showAndWait();
        }
    }
}
