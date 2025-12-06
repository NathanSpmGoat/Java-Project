package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Se connecter"
     * Récupère les informations et les passe au service d'authentification
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println(" Données récupérées depuis la vue :");
        System.out.println("Username : " + username);
        System.out.println("Password : " + password);

    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Pas encore de compte ? Créer"
     */
    @FXML
    private void goToRegister() {
        System.out.println(" Navigation vers la page Register (à implémenter plus tard)");
    }
}
