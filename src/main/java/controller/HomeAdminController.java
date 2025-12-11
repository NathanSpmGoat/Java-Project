package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;
import model.services.ReservationService;
import model.services.UserService;
import model.services.VehiculeService;
import utils.Navigation;

import java.util.List;

public class HomeAdminController {

    // Table des véhicules
    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, Integer> colId;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrixJournalier;
    @FXML private TableColumn<Vehicule, String> colEtat;

    // Boutons
    @FXML private Button btnAjouter;
    @FXML private Button btnBack;

    // Labels statistiques
    @FXML private Label lblVehicules;
    @FXML private Label lblReservations;
    @FXML private Label lblUsers;

    // Services
    private final VehiculeService vehiculeService = new VehiculeService();
    private final ReservationService reservationService = new ReservationService();
    private final UserService userService = new UserService();

    // ObservableList pour la table
    private final ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Liaison des colonnes de la table avec les propriétés des objets Vehicule
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        tableVehicules.setItems(vehiculesList);

        // Actions des boutons
        btnAjouter.setOnAction(e -> ajouterVehicule());
        btnBack.setOnAction(e -> Navigation.goTo("dashboard.fxml", btnBack));

        // Charger les données initiales
        loadData();
    }

    /**
     * Charge les véhicules, réservations et utilisateurs pour mettre à jour les labels et la table
     */
    private void loadData() {
        try {
            // Véhicules
            vehiculesList.setAll(vehiculeService.getAllVehicules());
            lblVehicules.setText(String.valueOf(vehiculesList.size()));

            // Réservations actives (statut = "confirmée")
            List<Reservation> reservations = reservationService.getAllReservations();
            long activeCount = reservations.stream()
                    .filter(r -> "confirmée".equalsIgnoreCase(r.getStatut()))
                    .count();
            lblReservations.setText(String.valueOf(activeCount));

            // Utilisateurs inscrits
            lblUsers.setText(String.valueOf(userService.getAllUsers().size()));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    /**
     * Ajouter un véhicule via une saisie rapide (TextInputDialog)
     * Après ajout, recharge les données pour mettre à jour la table et les labels
     */
    private void ajouterVehicule() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ajouter un nouveau véhicule");
        dialog.setContentText("Marque, Modèle, Type, Prix/jour, État (séparés par des virgules) :");
        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                if (parts.length < 5) throw new IllegalArgumentException("Toutes les informations sont requises");

                Vehicule v = new Vehicule();
                v.setMarque(parts[0].trim());
                v.setModele(parts[1].trim());
                v.setType(parts[2].trim());
                v.setPrixJournalier(Double.parseDouble(parts[3].trim()));
                v.setEtat(parts[4].trim());

                int newId = vehiculeService.addVehicule(v);
                v.setId(newId);

                // Recharge les données pour mettre à jour table et labels
                loadData();

            } catch (Exception e) {
                showAlert("Erreur", "Impossible d'ajouter le véhicule : " + e.getMessage());
            }
        });
    }

    /**
     * Affiche un message d'information à l'utilisateur
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}