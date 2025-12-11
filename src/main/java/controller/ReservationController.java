package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;
import utils.Navigation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationController {

    @FXML private TableView<Reservation> tableReservations;

    @FXML private TableColumn<Reservation, Integer> colId;
    @FXML private TableColumn<Reservation, Utilisateur> colUtilisateur;
    @FXML private TableColumn<Reservation, Vehicule> colVehicule;
    @FXML private TableColumn<Reservation, LocalDateTime> colDateDebut;
    @FXML private TableColumn<Reservation, LocalDateTime> colDateFin;
    @FXML private TableColumn<Reservation, String> colStatus;
    @FXML private TableColumn<Reservation, Double> colMontant;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnBack;

    private ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));

        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        colUtilisateur.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Utilisateur user, boolean empty) {
                super.updateItem(user, empty);
                setText((empty || user == null) ? "" : user.getNom() + " " + user.getPrenom());
            }
        });

        colVehicule.setCellValueFactory(new PropertyValueFactory<>("vehicule"));
        colVehicule.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Vehicule v, boolean empty) {
                super.updateItem(v, empty);
                setText((empty || v == null) ? "" : v.getMarque() + " " + v.getModele());
            }
        });

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateDebut.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime d, boolean empty) {
                super.updateItem(d, empty);
                setText((empty || d == null) ? "" : d.format(format));
            }
        });

        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colDateFin.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime d, boolean empty) {
                super.updateItem(d, empty);
                setText((empty || d == null) ? "" : d.format(format));
            }
        });

        tableReservations.setItems(reservationsList);

        btnAjouter.setOnAction(e -> ajouterReservation());
        btnModifier.setOnAction(e -> modifierReservation());
        btnSupprimer.setOnAction(e -> supprimerReservation());
        btnBack.setOnAction(e -> Navigation.goTo("dashboard.fxml", btnBack));

    }

    private void ajouterReservation() {
        System.out.println("Ajouter réservation");
    }

    private void modifierReservation() {
        Reservation r = tableReservations.getSelectionModel().getSelectedItem();
        if (r != null) System.out.println("Modifier réservation ID : " + r.getId());
    }

    private void supprimerReservation() {
        Reservation r = tableReservations.getSelectionModel().getSelectedItem();
        if (r != null) {
            reservationsList.remove(r);
            System.out.println("Supprimée : " + r.getId());
        }
    }
}
