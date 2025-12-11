package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Vehicule;
import model.services.VehiculeService;
import utils.Navigation;

public class HomeAdminController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, Integer> colId;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrixJournalier;
    @FXML private TableColumn<Vehicule, String> colEtat;

    @FXML private Button btnAjouter;
    @FXML private Button btnBack;

    @FXML private Label lblVehicules;
    @FXML private Label lblReservations;
    @FXML private Label lblUsers;

    private final VehiculeService vehiculeService = new VehiculeService();
    private final ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Liaison des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        tableVehicules.setItems(vehiculesList);

        // Actions boutons
        btnAjouter.setOnAction(e -> ajouterVehicule());
        btnBack.setOnAction(e -> Navigation.goTo("dashboard.fxml", btnBack));

        // Charger les véhicules
        loadVehicules();
    }

    private void loadVehicules() {
        try {
            vehiculesList.setAll(vehiculeService.getAllVehicules());
            lblVehicules.setText(String.valueOf(vehiculesList.size()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les véhicules : " + e.getMessage());
        }
    }

    private void ajouterVehicule() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ajouter un nouveau véhicule");
        dialog.setContentText("Marque, Modèle, Type, Prix/jour, État (séparés par des virgules) :");
        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                if (parts.length < 5) throw new IllegalArgumentException("Toutes les informations sont requises");

                Vehicule v = new Vehicule();
                v.setMarque(parts[0].trim());
                v.setModele(parts[1].trim());
                v.setType(parts[2].trim());
                v.setPrixJournalier(Double.parseDouble(parts[3].trim()));
                v.setEtat(parts[4].trim());

                int newId = vehiculeService.addVehicule(v);
                v.setId(newId);
                vehiculesList.add(v);
                lblVehicules.setText(String.valueOf(vehiculesList.size()));

            } catch (Exception e) {
                showAlert("Erreur", "Impossible d'ajouter le véhicule : " + e.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
