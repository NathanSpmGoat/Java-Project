package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.services.ReservationService;
import model.services.UserService;
import model.services.VehiculeService;
import utils.Navigation;

import java.sql.SQLException;

public class DashboardController {
    public Button btnHomeAdmin;

    // --- Boutons de navigation ---

    @FXML private Button btnVehicules;
    @FXML private Button btnReservations;
    @FXML private Button btnUsers;
    @FXML private Button btnLogout;

    // --- Labels pour afficher les compteurs ---
    @FXML private Label lblVehicules;
    @FXML private Label lblReservations;
    @FXML private Label lblUsers;

    // --- Services pour récupérer les données depuis la base ---
    private final VehiculeService vehiculeService = new VehiculeService();
    private final ReservationService reservationService = new ReservationService();
    private final UserService utilisateurService = new UserService(); // instance pour utiliser les méthodes non-statiques

    @FXML
    public void initialize() {
        // Charger les vraies valeurs depuis la base de données
        loadCounts();

        // Configurer la navigation pour chaque bouton

        setupNavigation(btnVehicules, "vehicules.fxml");
        setupNavigation(btnReservations, "reservations.fxml");
        setupNavigation(btnUsers, "users.fxml");
        setupNavigation(btnLogout, "login.fxml");
        setupNavigation(btnHomeAdmin, "homeAdmin.fxml");
    }

    /**
     * Charge les compteurs depuis la base et met à jour les labels.
     */
    private void loadCounts() {
        try {
            // Récupération du nombre de véhicules
            int nbVehicules = vehiculeService.getAllVehicules().size();

            // Récupération du nombre de réservations
            int nbReservations = reservationService.getAllReservations().size();

            // Récupération du nombre d'utilisateurs

            int nbUsers = utilisateurService.getAllUsers().size();

            // Mise à jour des labels
            lblVehicules.setText(String.valueOf(nbVehicules));
            lblReservations.setText(String.valueOf(nbReservations));
            lblUsers.setText(String.valueOf(nbUsers));
        } catch (SQLException e) {
            // En cas d'erreur, on affiche 0 pour tous les compteurs
            e.printStackTrace();
            lblVehicules.setText("0");
            lblReservations.setText("0");
            lblUsers.setText("0");
        }
    }

    /**
     * Configure un bouton pour naviguer vers une autre page FXML.
     * @param button Le bouton sur lequel l'action sera attachée.
     * @param fxmlFile Le fichier FXML vers lequel naviguer.
     */
    private void setupNavigation(Button button, String fxmlFile) {
        button.setOnAction(e -> Navigation.goTo(fxmlFile, button));
    }
}
