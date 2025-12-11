package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Vehicule;
import model.services.VehiculeService;
import utils.Navigation;

public class VehiculeController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, Integer> colId;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrixJournalier;
    @FXML private TableColumn<Vehicule, String> colEtat;
    @FXML private TableColumn<Vehicule, Void> colAction; // Colonne bouton Réserver

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnBack;

    private final VehiculeService vehiculeService = new VehiculeService();
    private final ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Liaison colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Coloration de la colonne Etat
        colEtat.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String etat, boolean empty) {
                super.updateItem(etat, empty);
                if (empty || etat == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(etat);
                    if ("RESERVE".equalsIgnoreCase(etat)) {
                        setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                    }
                }
            }
        });

        // Colonne bouton "Réserver"
        addButtonToTable();

        // TableView
        tableVehicules.setItems(vehiculesList);

        // Boutons
        btnAjouter.setOnAction(e -> ajouter());
        btnModifier.setOnAction(e -> modifier());
        btnSupprimer.setOnAction(e -> supprimer());
        btnBack.setOnAction(e -> Navigation.goTo("dashboard.fxml", btnBack));

        loadVehicules();
    }

    private void addButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Réserver");

            {
                btn.setOnAction(event -> {
                    Vehicule v = getTableView().getItems().get(getIndex());
                    if (!"DISPONIBLE".equalsIgnoreCase(v.getEtat())) {
                        showAlert("Attention", "Ce véhicule est déjà réservé !");
                        return;
                    }

                    try {
                        // Mise à jour immédiate dans TableView
                        v.setEtat("RESERVE");
                        tableVehicules.refresh(); // rafraîchissement immédiat

                        // Mise à jour dans la base de données
                        vehiculeService.setEtat(v.getId(), "RESERVE");

                        showAlert("Succès", "Véhicule réservé !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert("Erreur", "Impossible de réserver : " + ex.getMessage());
                        // Revenir à l'état initial en cas d'erreur
                        v.setEtat("DISPONIBLE");
                        tableVehicules.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Vehicule v = getTableView().getItems().get(getIndex());
                    btn.setDisable(!"DISPONIBLE".equalsIgnoreCase(v.getEtat()));
                    setGraphic(btn);
                }
            }
        });
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
