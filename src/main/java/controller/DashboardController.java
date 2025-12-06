package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    // Boutons de navigation
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnVehicules;

    @FXML
    private Button btnReservations;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnLogout;

    // Labels de résumé
    @FXML
    private Label lblVehicules;

    @FXML
    private Label lblReservations;

    @FXML
    private Label lblUsers;

    @FXML
    public void initialize() {
        // Valeurs statiques pour le moment
        lblVehicules.setText("12");
        lblReservations.setText("5");
        lblUsers.setText("20");

        // Gestion des clics sur les boutons
        btnVehicules.setOnAction(event -> openVehicules());
        btnReservations.setOnAction(event -> openReservations());
        btnUsers.setOnAction(event -> openUsers());
        btnLogout.setOnAction(event -> logout());
    }

    // Méthodes de navigation
    private void openVehicules() {
        System.out.println("Navigation vers Véhicules");
    }

    private void openReservations() {
        System.out.println("Navigation vers Réservations");
    }

    private void openUsers() {
        System.out.println("Navigation vers Utilisateurs");
    }

    private void logout() {
        System.out.println("Déconnexion...");

    }
}
