package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.entities.Utilisateur;
import model.services.AuthService;
import utils.Navigation;

public class RegisterController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Hyperlink loginLink;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleCreateAccount() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        if (!pass.equals(confirm)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas");
            return;
        }

        try {
            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setEmail(email);

            boolean ok = authService.register(u, pass);
            if (ok) {
                showAlert("Succès", "Compte créé avec succès !");
                Navigation.goTo("login.fxml", loginLink);
            } else {
                showAlert("Erreur", "Email déjà utilisé");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de créer le compte : " + e.getMessage());
        }
    }

    @FXML
    private void goToLogin() {
        Navigation.goTo("login.fxml", loginLink);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
