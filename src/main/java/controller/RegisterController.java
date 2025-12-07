package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleCreateAccount() {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // vérification + UserService.register(...)
        System.out.println("Création de compte : " + nom + " " + prenom + " (" + email + ")");
    }

    @FXML
    private void goToLogin() {
        System.out.println("➡ Retour à Login.fxml");
    }
}
