package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.entities.Utilisateur;
import model.services.AuthService;
import utils.Navigation;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button btnLogin;
    @FXML private Hyperlink registerLink;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> handleLogin());
        registerLink.setOnAction(e -> goToRegister());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez renseigner tous les champs !");
            return;
        }

        try {
            Utilisateur user = authService.login(email, password);

            if (user != null) {
                String role = user.getRole() != null ? user.getRole().toUpperCase() : "USER";

                if ("ADMIN".equals(role)) {
                    Navigation.goTo("HomeAdmin.fxml", btnLogin);
                } else {
                    Navigation.goTo("homeUtilisateur.fxml", btnLogin);
                }
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect !");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Erreur", "Impossible de se connecter : " + ex.getMessage());
        }
    }

    @FXML
    private void goToRegister() {
        Navigation.goTo("register.fxml", registerLink);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
