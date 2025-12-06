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

    /**
     * Méthode appelée quand l'utilisateur clique sur "Créer un compte"
     */
    @FXML
    private void handleCreateAccount() {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        System.out.println("➡ Données récupérées depuis la vue Register :");
        System.out.println("Nom : " + nom);
        System.out.println("Prénom : " + prenom);
        System.out.println("Email : " + email);

    }

    /**
     * Retour vers la page de connexion
     */
    @FXML
    private void goToLogin() {
        System.out.println("➡ Navigation vers la page Login (à implémenter plus tard)");

        // Plus tard : Charger login.fxml
    }
}
