package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Utilisateur;
import model.services.UserService;

public class EditUserController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtRole;
    @FXML private PasswordField txtMotDePasse;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private final UserService userService = new UserService();
    private Utilisateur utilisateur; // l'utilisateur à modifier

    @FXML
    private void initialize() {
        // Bouton modifier
        btnModifier.setOnAction(e -> modifierUtilisateur());

        // Annuler
        btnAnnuler.setOnAction(e -> {
            Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Méthode pour remplir le formulaire avec l'utilisateur sélectionné
     */
    public void setUtilisateur(Utilisateur u) {
        this.utilisateur = u;
        txtNom.setText(u.getNom());
        txtPrenom.setText(u.getPrenom());
        txtEmail.setText(u.getEmail());
        txtRole.setText(u.getRole());
        txtMotDePasse.setText(u.getMotDePasse());
    }

    private void modifierUtilisateur() {
        try {
            utilisateur.setNom(txtNom.getText().trim());
            utilisateur.setPrenom(txtPrenom.getText().trim());
            utilisateur.setEmail(txtEmail.getText().trim());
            utilisateur.setRole(txtRole.getText().trim().isEmpty() ? "UTILISATEUR" : txtRole.getText().trim());
            utilisateur.setMotDePasse(txtMotDePasse.getText().trim());

            if (userService.updateUser(utilisateur)) {
                showAlert("Succès", "Utilisateur modifié !");
            }

            // Fermer le formulaire
            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.close();

        } catch (Exception ex) {
            showAlert("Erreur", "Impossible de modifier l'utilisateur : " + ex.getMessage());
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