package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import model.entities.Utilisateur;
import model.services.UserService;


public class EditUserController {

    @FXML private ComboBox<String> cmbRole;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private final UserService userService = new UserService();
    private Utilisateur utilisateur;

    /**
     * Initialisation du controller.
     * Remplit la ComboBox et configure les actions des boutons.
     */
    @FXML
    private void initialize() {
        cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "UTILISATEUR"));
        btnModifier.setOnAction(e -> modifierRole());
        btnAnnuler.setOnAction(e -> closeWindow());
    }

    /**
     * Remplit le formulaire avec l'utilisateur sélectionné.
     */
    public void setUtilisateur(Utilisateur u) {
        this.utilisateur = u;
        cmbRole.setValue(u.getRole()); // sélection actuelle
    }

    /**
     * Modifie le rôle de l'utilisateur et met à jour la base.
     */
    private void modifierRole() {
        String role = cmbRole.getValue();
        if (role == null || role.isEmpty()) role = "UTILISATEUR";

        try {
            utilisateur.setRole(role);
            if (userService.updateUser(utilisateur)) {
                showAlert("Succès", "Rôle modifié !");
            }
            closeWindow();
        } catch (Exception ex) {
            showAlert("Erreur", "Impossible de modifier le rôle : " + ex.getMessage());
        }
    }

    /**
     * Ferme la fenêtre.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    /**
     * Affiche une alerte d'information.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
