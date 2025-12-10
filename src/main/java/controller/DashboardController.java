package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utils.Navigation;

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
        // Valeurs d'exemple
        lblVehicules.setText("0");
        lblReservations.setText("0");
        lblUsers.setText("0");

        // Appel générique pour tous les boutons
        setupNavigation(btnDashboard, "homeUtilisateur.fxml");
        setupNavigation(btnVehicules, "vehicules.fxml");
        setupNavigation(btnReservations, "reservations.fxml");
        setupNavigation(btnUsers, "users.fxml");
        setupNavigation(btnLogout, "login.fxml");
    }

    private void setupNavigation(Button button, String fxmlFile) {
        button.setOnAction(e -> Navigation.goTo(fxmlFile, button));
    }
}
