package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Utilisateur;
import model.services.UserService;
import utils.Navigation;

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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableUsers.setItems(usersList);

        loadUsers();

        btnAjouter.setOnAction(e -> ajouter());
        btnModifier.setOnAction(e -> modifier());
        btnSupprimer.setOnAction(e -> supprimer());
        btnBack.setOnAction(e -> Navigation.goTo("dashboard.fxml", btnBack));

    }

    private void loadUsers() {
        try {
            usersList.setAll(userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les utilisateurs : " + e.getMessage());
        }
    }

    private void ajouter() {
        // Ouvrir un formulaire de création ou modal
        System.out.println("Ajouter utilisateur...");
    }

    private void modifier() {
        Utilisateur u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null) {
            System.out.println("Modifier utilisateur : " + u.getNom());
        } else {
            showAlert("Attention", "Veuillez sélectionner un utilisateur.");
        }
    }

    private void supprimer() {
        Utilisateur u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null) {
            try {
                if (userService.deleteUser(u.getId())) {
                    usersList.remove(u);
                    System.out.println("Supprimé : " + u.getNom());
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de supprimer l'utilisateur : " + e.getMessage());
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un utilisateur.");
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
