package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import model.entities.Vehicule;
import model.services.VehiculeService;

import java.sql.SQLException;

public class HomeUtilisateurController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colType;
    @FXML private TableColumn<Vehicule, Double> colPrix;
    @FXML private TableColumn<Vehicule, String> colEtat;
    @FXML private TableColumn<Vehicule, Void> colAction;

    private final VehiculeService vehiculeService = new VehiculeService();

    @FXML
    public void initialize() throws SQLException {

        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        addReservationButton();

        // Remplir tableau
        tableVehicules.setItems(FXCollections.observableArrayList(
                vehiculeService.getAllVehicules()
        ));
    }

    private void addReservationButton() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Réserver");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                btn.setOnAction(e -> {
                    Vehicule selected = getTableView().getItems().get(getIndex());
                    System.out.println("Réservation demandée pour : " + selected.getMarque());

                    // plus tard : ouvrir reservationUtilisateur.fxml
                });

                setGraphic(btn);
            }
        });
    }
}
