package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Plus tard : appel AuthService.login(username, password)
        System.out.println("Tentative de connexion : " + username);
    }

    @FXML
    private void goToRegister() {
        System.out.println("➡ Navigation vers Register.fxml");
    }
}
