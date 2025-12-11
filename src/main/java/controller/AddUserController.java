package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Utilisateur;
import model.services.UserService;

public class AddUserController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtRole;
    @FXML private PasswordField txtMotDePasse;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        // Ajouter un utilisateur
        btnAjouter.setOnAction(e -> ajouterUtilisateur());

        // Annuler et fermer la fenêtre
        btnAnnuler.setOnAction(e -> {
            Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            stage.close();
        });
    }

    private void ajouterUtilisateur() {
        try {
            // Créer un nouvel utilisateur avec les valeurs saisies
            Utilisateur u = new Utilisateur();
            u.setNom(txtNom.getText().trim());
            u.setPrenom(txtPrenom.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setRole(txtRole.getText().trim().isEmpty() ? "UTILISATEUR" : txtRole.getText().trim());
            u.setMotDePasse(txtMotDePasse.getText().trim()); // Mot de passe à stocker (idéalement hashé)

            int newId = userService.createUser(u); // ajoute dans la DB
            u.setId(newId);

            showAlert("Succès", "Utilisateur ajouté !");

            // Fermer le formulaire
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            stage.close();

        } catch (Exception ex) {
            showAlert("Erreur", "Impossible d'ajouter l'utilisateur : " + ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}