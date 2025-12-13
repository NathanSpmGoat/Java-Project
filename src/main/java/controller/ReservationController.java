package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;
import model.services.ReservationService;
import model.services.PdfGeneratorService;
import utils.Navigation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationController {

    @FXML private TableView<Reservation> tableReservations;
    @FXML private TableColumn<Reservation, Integer> colId;
    @FXML private TableColumn<Reservation, Utilisateur> colUtilisateur;
    @FXML private TableColumn<Reservation, Vehicule> colVehicule;
    @FXML private TableColumn<Reservation, LocalDate> colDateDebut;
    @FXML private TableColumn<Reservation, LocalDate> colDateFin;
    @FXML private TableColumn<Reservation, String> colStatus;
    @FXML private TableColumn<Reservation, Double> colMontant;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnBack;
    @FXML private Button btnExportPDF;

    private final ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();
    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {

        // Colonnes simples
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));

        // Afficher nom complet de l'utilisateur
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        colUtilisateur.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Utilisateur user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "" : user.getNom() + " " + user.getPrenom());
            }
        });

        // Affichage véhicule
        colVehicule.setCellValueFactory(new PropertyValueFactory<>("vehicule"));
        colVehicule.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Vehicule v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : v.getMarque() + " " + v.getModele());
            }
        });

        // Formatage dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateDebut.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? "" : d.format(formatter));
            }
        });

        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colDateFin.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? "" : d.format(formatter));
            }
        });

        // TableView
        tableReservations.setItems(reservationsList);
        loadReservations();

        // Actions
        btnAjouter.setOnAction(e -> openAddReservation());
        btnModifier.setOnAction(e -> openEditReservation());
        btnSupprimer.setOnAction(e -> deleteReservation());
        btnBack.setOnAction(e -> backToDashboard());
        btnExportPDF.setOnAction(e -> exportAllPDFs());
    }

    /** Charge toutes les réservations */
    private void loadReservations() {
        try {
            List<Reservation> all = reservationService.getAllReservations();
            reservationsList.setAll(all);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de récupérer les réservations.");
        }
    }

    /** Pour ajouter une réservaton */
    private void openAddReservation() {
        Navigation.openModal("addReservationForm.fxml", "Ajouter une réservation");
        loadReservations();
    }

    /** Ouvre editReservation via Navigation + passage de données */
    private void openEditReservation() {
        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Sélection", "Veuillez sélectionner une réservation.");
            return;
        }

        Navigation.openModalWithData(
                "editReservation.fxml",
                "Modifier une réservation",
                (EditReservationController controller) -> controller.setReservation(selected)
        );

        loadReservations();
    }

    /** Supprime une réservation */
    private void deleteReservation() {
        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection", "Veuillez sélectionner une réservation à annuler.");
            return;
        }

        try {
            boolean ok = reservationService.annulerReservation(selected.getId());
            if (ok) {
                showAlert("Succès", "Réservation annulée.");
                loadReservations();
            } else {
                showAlert("Erreur", "Impossible d'annuler cette réservation.");
            }

        } catch (SQLException | IllegalStateException ex) {
            showAlert("Erreur", ex.getMessage());
        }
    }

    /** Export PDF */
    private void exportAllPDFs() {
        if (reservationsList.isEmpty()) {
            showAlert("Export PDF", "Aucune réservation à exporter.");
            return;
        }

        for (Reservation r : reservationsList) {
            PdfGeneratorService.generateReservationPDF(r, r.getUtilisateur(), r.getVehicule());
        }

        showAlert("Export PDF", "Tous les PDFs ont été générés dans /download.");
    }

    /** Retour Dashboard */
    private void backToDashboard() {
        Navigation.goTo("dashboard.fxml", btnBack);
    }

    /** Alerte simple */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
