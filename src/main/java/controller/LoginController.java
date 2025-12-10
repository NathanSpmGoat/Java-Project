package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.entities.Utilisateur;
import model.services.AuthService;
import utils.Navigation;

public class LoginController {

    @FXML private TextField usernameField;
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
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            Utilisateur user = authService.login(email, password);
            if (user != null) {
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    Navigation.goTo("homeAdmin.fxml", btnLogin);
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
