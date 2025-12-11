package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Utilisateur;
import model.services.UserService;
import utils.Navigation;

import java.io.IOException;

public class UserController {

    @FXML private TableView<Utilisateur> tableUsers;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colRole;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnBack;

    private final UserService userService = new UserService();
    private final ObservableList<Utilisateur> usersList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableUsers.setItems(usersList);

        // Charger les utilisateurs depuis la base
        loadUsers();

        // Associer les boutons à leurs actions
        btnAjouter.setOnAction(e -> ajouter());
        btnModifier.setOnAction(e -> modifier());
        btnSupprimer.setOnAction(e -> supprimer());
        btnBack.setOnAction(e -> Navigation.goTo("dashboard.fxml", btnBack));
    }

    /**
     * Recharge la table des utilisateurs depuis la base.
     */
    private void loadUsers() {
        try {
            usersList.setAll(userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les utilisateurs : " + e.getMessage());
        }
    }

    /**
     * Ouvre le formulaire d'ajout d'un nouvel utilisateur.
     */
    private void ajouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddUser.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un utilisateur");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // attend la fermeture

            // Recharger la table après ajout
            loadUsers();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout : " + e.getMessage());
        }
    }

    /**
     * Ouvre le formulaire de modification pour l'utilisateur sélectionné.
     */
    private void modifier() {
        Utilisateur u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditUser.fxml"));
                Parent root = loader.load();

                // Passer l'utilisateur au controller du formulaire
                EditUserController controller = loader.getController();
                controller.setUtilisateur(u);

                Stage stage = new Stage();
                stage.setTitle("Modifier utilisateur");
                stage.setScene(new Scene(root));
                stage.showAndWait(); // attend fermeture

                // Recharger la table après modification
                loadUsers();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir le formulaire de modification : " + e.getMessage());
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un utilisateur.");
        }
    }

    /**
     * Supprime l'utilisateur sélectionné après confirmation.
     */
    private void supprimer() {
        Utilisateur u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText(null);
            confirm.setContentText("Voulez-vous vraiment supprimer " + u.getNom() + " ?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        if (userService.deleteUser(u.getId())) {
                            usersList.remove(u);
                            showAlert("Succès", "Utilisateur supprimé !");
                        } else {
                            showAlert("Erreur", "Impossible de supprimer l'utilisateur.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Erreur", "Impossible de supprimer l'utilisateur : " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert("Attention", "Veuillez sélectionner un utilisateur.");
        }
    }

    /**
     * Affiche une boîte de dialogue d'information.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}