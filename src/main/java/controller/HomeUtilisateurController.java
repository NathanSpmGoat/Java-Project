package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import model.entities.Vehicule;
import model.services.VehiculeService;
import utils.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HomeUtilisateurController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colMatricule;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrix;
    @FXML private TableColumn<Vehicule, String> colEtat;
    @FXML private TableColumn<Vehicule, Void> colAction;

    @FXML private Button btnProfil;
    @FXML private Button btnLogout;

    private final VehiculeService vehiculeService = new VehiculeService();

    @FXML
    public void initialize() {

        // Colonnes
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Couleur état
        colEtat.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String etat, boolean empty) {
                super.updateItem(etat, empty);
                if (empty || etat == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(etat);
                    if ("RESERVE".equalsIgnoreCase(etat)) {
                        setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                    }
                }
            }
        });

        // Bouton Réserver
        addReservationButton();

        // Couleur ligne
        tableVehicules.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if ("RESERVE".equalsIgnoreCase(item.getEtat())) {
                    setStyle("-fx-background-color: rgba(231,76,60,0.2);");
                } else {
                    setStyle("");
                }
            }
        });

        // Actions
        btnProfil.setOnAction(e -> openProfil());
        btnLogout.setOnAction(e -> handleLogout());

        refreshTable();
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

                Vehicule v = getTableView().getItems().get(getIndex());
                btn.setDisable(!"DISPONIBLE".equalsIgnoreCase(v.getEtat()));

                btn.setOnAction(e -> openReservationDialog(v));

                setGraphic(btn);
            }
        });
    }

    private void openReservationDialog(Vehicule vehicule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reservation_form.fxml"));
            Scene scene = new Scene(loader.load());

            controller.ReservationFormController controller = loader.getController();
            controller.setVehicule(vehicule);

            Stage stage = new Stage();
            stage.setTitle("Réserver : " + vehicule.getMarque() + " " + vehicule.getModele());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHiding(event -> refreshTable());
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la réservation.");
        }
    }

    //  Version corrigée pour ouvrir le profil
    private void openProfil() {
        try {
            // Récupérer l'utilisateur courant depuis la session
            model.entities.Utilisateur user = Session.getCurrentUser();
            if (user == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profil.fxml"));
            Scene scene = new Scene(loader.load());

            controller.ProfilController controller = loader.getController();
            controller.setUtilisateur(user);

            Stage stage = new Stage();
            stage.setTitle("Profil : " + user.getNom());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page Profil.");
        }
    }

    private void refreshTable() {
        try {
            List<Vehicule> list = vehiculeService.getAllVehicules();
            tableVehicules.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les véhicules.");
        }
    }

    @FXML
    private void handleLogout() {
        Session.clear();
        utils.Navigation.goTo("login.fxml", btnLogout);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
