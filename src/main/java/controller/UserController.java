package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Utilisateur;

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

    private ObservableList<Utilisateur> usersList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableUsers.setItems(usersList);

        btnAjouter.setOnAction(e -> ajouter());
        btnModifier.setOnAction(e -> modifier());
        btnSupprimer.setOnAction(e -> supprimer());
    }

    private void ajouter() {
        System.out.println("Ajouter utilisateur");
    }

    private void modifier() {
        Utilisateur u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null) System.out.println("Modifier utilisateur : " + u.getNom());
    }

    private void supprimer() {
        Utilisateur u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null) {
            usersList.remove(u);
            System.out.println("Supprimé : " + u.getNom());
        }
    }
}
