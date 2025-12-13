package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.control.TableRow;
import model.entities.Vehicule;
import model.services.VehiculeService;
import utils.Navigation;

public class VehiculeController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, Integer> colId;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colMatricule;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrixJournalier;
    @FXML private TableColumn<Vehicule, String> colEtat;
    @FXML private TableColumn<Vehicule, Void> colAction;

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
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Couleur cellule état
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

        // Coloration de toute la ligne selon l’état
        tableVehicules.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if ("RESERVE".equalsIgnoreCase(item.getEtat())) {
                        setStyle("-fx-background-color: rgba(231,76,60,0.3);");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Bouton "Réserver"
        addButtonToTable();

        tableVehicules.setItems(vehiculesList);

        // Actions boutons
        btnAjouter.setOnAction(e -> openAddVehicule());
        btnModifier.setOnAction(e -> openEditVehicule());
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
                        v.setEtat("RESERVE");
                        tableVehicules.refresh();
                        vehiculeService.setEtat(v.getId(), "RESERVE");
                        showAlert("Succès", "Véhicule réservé !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        v.setEtat("DISPONIBLE");
                        tableVehicules.refresh();
                        showAlert("Erreur", "Impossible de réserver : " + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
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

    private void openAddVehicule() {
        Navigation.openModal("addVehicule.fxml", "Ajouter un véhicule");
        loadVehicules();
    }

    private void openEditVehicule() {
        Vehicule selected = tableVehicules.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un véhicule.");
            return;
        }
        Navigation.openModalWithData(
                "editVehicule.fxml",
                "Modifier un véhicule",
                (EditVehiculeController controller) -> controller.setVehicule(selected)
        );
        loadVehicules();
    }

    private void supprimer() {
        Vehicule v = tableVehicules.getSelectionModel().getSelectedItem();
        if (v == null) {
            showAlert("Attention", "Veuillez sélectionner un véhicule.");
            return;
        }

        if ("RESERVE".equalsIgnoreCase(v.getEtat())) {
            showAlert("Attention", "Impossible de supprimer un véhicule réservé !");
            return;
        }

        try {
            if (vehiculeService.deleteVehicule(v.getId())) {
                vehiculesList.remove(v);
            } else {
                showAlert("Erreur", "Impossible de supprimer.");
            }
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
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
