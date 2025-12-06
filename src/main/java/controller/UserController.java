package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Utilisateur;

public class UserController {

    @FXML
    private TableView<Utilisateur> tableUsers;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, String> colEmail;

    @FXML
    private TableColumn<Utilisateur, String> colRole;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private ObservableList<Utilisateur> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Ajouter des données fictives
        initData();

        tableUsers.setItems(usersList);

        // Boutons
        btnAjouter.setOnAction(e -> ajouterUtilisateur());
        btnModifier.setOnAction(e -> modifierUtilisateur());
        btnSupprimer.setOnAction(e -> supprimerUtilisateur());
    }

    private void initData() {
        Utilisateur u1 = new Utilisateur();
        u1.setId(1);
        u1.setNom("Alice");
        u1.setPrenom("Dupont");
        u1.setEmail("alice@example.com");
        u1.setRole("Client");

        Utilisateur u2 = new Utilisateur();
        u2.setId(2);
        u2.setNom("Bob");
        u2.setPrenom("Martin");
        u2.setEmail("bob@example.com");
        u2.setRole("Admin");

        usersList.addAll(u1, u2);
    }

    private void ajouterUtilisateur() {
        System.out.println("Ajouter un utilisateur");
    }

    private void modifierUtilisateur() {
        Utilisateur selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Modifier l'utilisateur : " + selected.getNom());
        }
    }

    private void supprimerUtilisateur() {
        Utilisateur selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Supprimer l'utilisateur : " + selected.getNom());
            usersList.remove(selected);
        }
    }
}
