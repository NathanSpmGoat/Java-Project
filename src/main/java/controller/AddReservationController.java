package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;
import model.services.ReservationService;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller pour ajouter une nouvelle réservation.
 */
public class AddReservationController {

    @FXML private ComboBox<Utilisateur> cmbUtilisateur;
    @FXML private ComboBox<Vehicule> cmbVehicule;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private Button btnValider;
    @FXML private Button btnAnnuler;

    private final ReservationService reservationService = new ReservationService();

    @FXML
    private void initialize() {
        // Charger les utilisateurs et véhicules depuis la DB
        try {
            cmbUtilisateur.setItems(FXCollections.observableArrayList(reservationService.getAllUtilisateurs()));
            cmbVehicule.setItems(FXCollections.observableArrayList(reservationService.getAllVehicules()));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les utilisateurs ou véhicules.");
        }

        // Actions des boutons
        btnValider.setOnAction(e -> addReservation());
        btnAnnuler.setOnAction(e -> closeWindow());
    }

    /** Ajoute la réservation à la DB */
    private void addReservation() {
        Utilisateur u = cmbUtilisateur.getValue();
        Vehicule v = cmbVehicule.getValue();
        LocalDate debut = dpDateDebut.getValue();
        LocalDate fin = dpDateFin.getValue();

        if (u == null || v == null || debut == null || fin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Reservation r = new Reservation();
        r.setUtilisateur(u);
        r.setVehicule(v);
        r.setDateDebut(debut);
        r.setDateFin(fin);

        try {
            reservationService.reserver(r);
            showAlert("Succès", "Réservation ajoutée !");
            closeWindow();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter la réservation : " + ex.getMessage());
        } catch (IllegalStateException | IllegalArgumentException ex) {
            showAlert("Erreur", ex.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
