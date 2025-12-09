package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.Navigation;
import utils.Session;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Hyperlink registerLink;


    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Plus tard : appel AuthService.login(username, password)
        System.out.println("Tentative de connexion : " + username);
    }

    @FXML
    private void goToRegister() {
        Navigation.goTo("register.fxml",registerLink);
        System.out.println("➡ Navigation vers Register.fxml");
    }
}
