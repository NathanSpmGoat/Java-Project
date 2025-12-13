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

public class EditReservationController {

    @FXML private ComboBox<Utilisateur> cmbUtilisateur;
    @FXML private ComboBox<Vehicule> cmbVehicule;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private Button btnValider;
    @FXML private Button btnAnnuler;

    private final ReservationService reservationService = new ReservationService();
    private Reservation reservation;

    /** Initialise la fenêtre et charge les listes */
    @FXML
    private void initialize() {
        try {
            cmbUtilisateur.setItems(FXCollections.observableArrayList(reservationService.getAllUtilisateurs()));
            cmbVehicule.setItems(FXCollections.observableArrayList(reservationService.getAllVehicules()));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les utilisateurs ou véhicules.");
        }

        btnValider.setOnAction(e -> updateReservation());
        btnAnnuler.setOnAction(e -> closeWindow());
    }

    /** Méthode pour pré-remplir les champs avec la réservation sélectionnée */
    public void setReservation(Reservation r) {
        this.reservation = r;
        cmbUtilisateur.setValue(r.getUtilisateur());
        cmbVehicule.setValue(r.getVehicule());
        dpDateDebut.setValue(r.getDateDebut());
        dpDateFin.setValue(r.getDateFin());
    }

    /** Modifie la réservation */
    private void updateReservation() {
        if (reservation == null) return;

        Utilisateur u = cmbUtilisateur.getValue();
        Vehicule v = cmbVehicule.getValue();
        LocalDate debut = dpDateDebut.getValue();
        LocalDate fin = dpDateFin.getValue();

        if (u == null || v == null || debut == null || fin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        reservation.setUtilisateur(u);
        reservation.setVehicule(v);
        reservation.setDateDebut(debut);
        reservation.setDateFin(fin);

        try {
            reservationService.modifierReservation(reservation);
            showAlert("Succès", "Réservation modifiée !");
            closeWindow();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Erreur", "Impossible de modifier la réservation : " + ex.getMessage());
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
