package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Vehicule;
import model.services.VehiculeService;

import java.sql.SQLException;

public class HomeAdminController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrix;
    @FXML private TableColumn<Vehicule, String> colEtat;
    @FXML private TableColumn<Vehicule, Void> colAction;
    @FXML private Button btnAddVehicule;

    private final VehiculeService vehiculeService = new VehiculeService();

    @FXML
    public void initialize() throws SQLException {
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        addActionButtons();

        loadVehicules();
    }

    private void loadVehicules() throws SQLException {
        tableVehicules.setItems(FXCollections.observableArrayList(
                vehiculeService.getAllVehicules()
        ));
    }

    private void addActionButtons() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnDelete = new Button("Supprimer");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                btnDelete.setOnAction(e -> {
                    Vehicule v = getTableView().getItems().get(getIndex());
                    try {
                        vehiculeService.deleteVehicule(v.getId());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        loadVehicules();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                setGraphic(btnDelete);
            }
        });
    }
}
