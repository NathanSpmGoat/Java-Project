package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;
import model.services.ReservationService;
import model.services.UserService;
import model.services.VehiculeService;
import utils.Session;

import java.io.IOException;
import java.util.List;

public class HomeAdminController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, Integer> colId;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrixJournalier;
    @FXML private TableColumn<Vehicule, String> colEtat;

    @FXML private Button btnAjouter;
    @FXML private Button btnBack;
    @FXML private Button btnLogout;
    @FXML private Button btnProfil;

    @FXML private Label lblVehicules;
    @FXML private Label lblReservations;
    @FXML private Label lblUsers;

    private final VehiculeService vehiculeService = new VehiculeService();
    private final ReservationService reservationService = new ReservationService();
    private final UserService userService = new UserService();

    private final ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        tableVehicules.setItems(vehiculesList);

        // Boutons
        btnAjouter.setOnAction(e -> ouvrirAddVehiculeModal());
        btnBack.setOnAction(e -> utils.Navigation.goTo("dashboard.fxml", btnBack));
        btnLogout.setOnAction(e -> handleLogout());
        if (btnProfil != null) btnProfil.setOnAction(e -> openProfil());

        loadData();
    }

    private void loadData() {
        try {
            vehiculesList.setAll(vehiculeService.getAllVehicules());
            lblVehicules.setText(String.valueOf(vehiculesList.size()));

            List<Reservation> reservations = reservationService.getAllReservations();
            long active = reservations.stream()
                    .filter(r -> "confirmée".equalsIgnoreCase(r.getStatut()))
                    .count();

            lblReservations.setText(String.valueOf(active));
            lblUsers.setText(String.valueOf(userService.getAllUsers().size()));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    /**
     *  Ouvre addVehicule.fxml en fenêtre modale
     */
    private void ouvrirAddVehiculeModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addVehicule.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Ajouter un véhicule");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale

            // Rafraîchir la table après fermeture
            stage.setOnHiding(event -> loadData());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout");
        }
    }

    private void openProfil() {
        try {
            Utilisateur user = Session.getCurrentUser();
            if (user == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profil.fxml"));
            Scene scene = new Scene(loader.load());

            ProfilController controller = loader.getController();
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
