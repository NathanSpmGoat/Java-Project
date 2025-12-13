package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Utilisateur;
import model.services.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Controller pour la page Profil avec hachage du mot de passe.
 * Admin peut modifier profil complet + mot de passe.
 * Utilisateur standard ne peut modifier que le mot de passe.
 */
public class ProfilController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtAncienMotDePasse;
    @FXML private PasswordField txtNouveauMotDePasse;
    @FXML private Label lblRole;
    @FXML private Button btnEnregistrer;
    @FXML private Button btnFermer;

    private final UserService userService = new UserService();
    private Utilisateur currentUser;

    public void setUtilisateur(Utilisateur user) {
        this.currentUser = user;
        if (currentUser != null) {
            txtNom.setText(currentUser.getNom());
            txtPrenom.setText(currentUser.getPrenom());
            txtEmail.setText(currentUser.getEmail());
            lblRole.setText(currentUser.getRole());

            // Admin peut modifier nom, prénom et email
            boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());

            txtNom.setEditable(isAdmin);
            txtPrenom.setEditable(isAdmin);
            txtEmail.setEditable(isAdmin);

            // Rôle toujours en lecture seule
            lblRole.setDisable(true);
        }
    }

    @FXML
    public void initialize() {
        btnEnregistrer.setOnAction(e -> handleSave());
        btnFermer.setOnAction(e -> closeWindow());
    }

    private void handleSave() {
        if (currentUser == null) return;

        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());

        // Si admin, on peut modifier nom, prénom, email
        if (isAdmin) {
            currentUser.setNom(txtNom.getText());
            currentUser.setPrenom(txtPrenom.getText());
            currentUser.setEmail(txtEmail.getText());
        }

        // Changement de mot de passe
        String ancien = txtAncienMotDePasse.getText();
        String nouveau = txtNouveauMotDePasse.getText();

        try {
            if (!ancien.isEmpty() || !nouveau.isEmpty()) {
                if (ancien.isEmpty() || nouveau.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir les deux champs de mot de passe.");
                    return;
                }

                String hashAncien = hashPassword(ancien);
                if (!hashAncien.equals(currentUser.getMotDePasse())) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "L'ancien mot de passe est incorrect.");
                    return;
                }

                currentUser.setMotDePasse(hashPassword(nouveau));
            }

            boolean success = userService.updateUser(currentUser);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour !");
                txtAncienMotDePasse.clear();
                txtNouveauMotDePasse.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour le profil.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur base de données", ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de hacher le mot de passe.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnFermer.getScene().getWindow();
        stage.close();
    }

    /** Alerte simple */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hachage du mot de passe avec SHA-256
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
