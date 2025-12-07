package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Button btnDashboard;
    @FXML private Button btnVehicules;
    @FXML private Button btnReservations;
    @FXML private Button btnUsers;
    @FXML private Button btnLogout;

    @FXML private Label lblVehicules;
    @FXML private Label lblReservations;
    @FXML private Label lblUsers;

    @FXML
    public void initialize() {
        // Ces valeurs seront chargées via un service plus tard
        lblVehicules.setText("0");
        lblReservations.setText("0");
        lblUsers.setText("0");

        btnVehicules.setOnAction(e -> openVehicules());
        btnReservations.setOnAction(e -> openReservations());
        btnUsers.setOnAction(e -> openUsers());
        btnLogout.setOnAction(e -> logout());
    }

    private void openVehicules() {
        System.out.println("➡ Navigation vers la gestion des véhicules");
    }

    private void openReservations() {
        System.out.println("➡ Navigation vers la gestion des réservations");
    }

    private void openUsers() {
        System.out.println("➡ Navigation vers la gestion des utilisateurs");
    }

    private void logout() {
        System.out.println("➡ Déconnexion...");
    }
}
