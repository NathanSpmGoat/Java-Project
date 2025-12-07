package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Vehicule;

public class VehiculeController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, Integer> colId;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrixJournalier;
    @FXML private TableColumn<Vehicule, String> colEtat;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;

    private ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        tableVehicules.setItems(vehiculesList);

        btnAjouter.setOnAction(e -> ajouter());
        btnModifier.setOnAction(e -> modifier());
        btnSupprimer.setOnAction(e -> supprimer());
    }

    private void ajouter() {
        System.out.println("Ajouter véhicule");
    }

    private void modifier() {
        Vehicule v = tableVehicules.getSelectionModel().getSelectedItem();
        if (v != null) System.out.println("Modifier véhicule : " + v.getMarque());
    }

    private void supprimer() {
        Vehicule v = tableVehicules.getSelectionModel().getSelectedItem();
        if (v != null) {
            vehiculesList.remove(v);
            System.out.println("Véhicule supprimé : " + v.getMarque());
        }
    }
}
