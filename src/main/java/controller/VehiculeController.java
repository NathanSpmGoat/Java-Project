package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Vehicule;
import model.services.VehiculeService;

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

    private final VehiculeService vehiculeService = new VehiculeService();
    private final ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

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

        loadVehicules();
    }

    private void loadVehicules() {
        try {
            vehiculesList.setAll(vehiculeService.getAllVehicules());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les véhicules : " + e.getMessage());
        }
    }

    private void ajouter() {
        // Ici tu pourrais ouvrir un formulaire pour saisir un nouveau véhicule
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ajouter un nouveau véhicule");
        dialog.setContentText("Marque, Modèle, Type, Prix/jour, État (séparés par des virgules):");
        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                if (parts.length < 5) throw new IllegalArgumentException("Toutes les infos sont requises");
                Vehicule v = new Vehicule();
                v.setMarque(parts[0].trim());
                v.setModele(parts[1].trim());
                v.setType(parts[2].trim());
                v.setPrixJournalier(Double.parseDouble(parts[3].trim()));
                v.setEtat(parts[4].trim());

                int newId = vehiculeService.addVehicule(v);
                v.setId(newId);
                vehiculesList.add(v);

            } catch (Exception e) {
                showAlert("Erreur", "Impossible d'ajouter le véhicule : " + e.getMessage());
            }
        });
    }

    private void modifier() {
        Vehicule v = tableVehicules.getSelectionModel().getSelectedItem();
        if (v != null) {
            TextInputDialog dialog = new TextInputDialog(v.getMarque() + "," + v.getModele() + "," + v.getType() + "," + v.getPrixJournalier() + "," + v.getEtat());
            dialog.setHeaderText("Modifier le véhicule");
            dialog.setContentText("Marque, Modèle, Type, Prix/jour, État (séparés par des virgules):");
            dialog.showAndWait().ifPresent(input -> {
                try {
                    String[] parts = input.split(",");
                    if (parts.length < 5) throw new IllegalArgumentException("Toutes les infos sont requises");
                    v.setMarque(parts[0].trim());
                    v.setModele(parts[1].trim());
                    v.setType(parts[2].trim());
                    v.setPrixJournalier(Double.parseDouble(parts[3].trim()));
                    v.setEtat(parts[4].trim());

                    if (vehiculeService.updateVehicule(v)) {
                        tableVehicules.refresh();
                    } else {
                        showAlert("Erreur", "Impossible de modifier le véhicule");
                    }
                } catch (Exception e) {
                    showAlert("Erreur", "Impossible de modifier le véhicule : " + e.getMessage());
                }
            });
        } else {
            showAlert("Attention", "Veuillez sélectionner un véhicule.");
        }
    }

    private void supprimer() {
        Vehicule v = tableVehicules.getSelectionModel().getSelectedItem();
        if (v != null) {
            try {
                if (vehiculeService.deleteVehicule(v.getId())) {
                    vehiculesList.remove(v);
                } else {
                    showAlert("Erreur", "Impossible de supprimer le véhicule");
                }
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de supprimer le véhicule : " + e.getMessage());
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un véhicule.");
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
