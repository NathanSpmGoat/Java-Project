package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import model.entities.Utilisateur;
import model.services.UserService;


public class AddUserController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtMotDePasse;
    @FXML private ComboBox<String> cmbRole;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    private final UserService userService = new UserService();

    /**
     * Initialisation du controller.
     * Remplit la ComboBox des rôles et configure les actions des boutons.
     */
    @FXML
    private void initialize() {
        cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "UTILISATEUR"));
        cmbRole.setValue("UTILISATEUR"); // rôle par défaut

        btnAjouter.setOnAction(e -> ajouterUtilisateur());
        btnAnnuler.setOnAction(e -> closeWindow());
    }

    /**
     * Crée un nouvel utilisateur avec les valeurs saisies dans le formulaire.
     * Affiche un message de succès ou d'erreur.
     */
    private void ajouterUtilisateur() {
        try {
            Utilisateur u = new Utilisateur();
            u.setNom(txtNom.getText().trim());
            u.setPrenom(txtPrenom.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setMotDePasse(txtMotDePasse.getText().trim());
            u.setRole(cmbRole.getValue() == null ? "UTILISATEUR" : cmbRole.getValue());

            int newId = userService.createUser(u);
            u.setId(newId);

            showAlert("Succès", "Utilisateur ajouté !");
            closeWindow();
        } catch (Exception ex) {
            showAlert("Erreur", "Impossible d'ajouter l'utilisateur : " + ex.getMessage());
        }
    }

    /**
     * Ferme la fenêtre du formulaire.
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
