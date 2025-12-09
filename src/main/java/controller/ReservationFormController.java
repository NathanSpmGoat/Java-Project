package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.entities.Reservation;
import model.entities.Vehicule;
import model.services.ReservationService;
import utils.Session;

import java.time.LocalDate;

public class ReservationFormController {

    @FXML private Label lblVehicule;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmer;

    private ReservationService reservationService = new ReservationService();
    private Vehicule vehicule;

    public void setVehicule(Vehicule v) {
        this.vehicule = v;
        lblVehicule.setText(v.getMarque() + " " + v.getModele());
    }

    @FXML
    public void initialize() {
        btnConfirmer.setOnAction(e -> reserver());
    }

    private void reserver() {
        try {
            Reservation r = new Reservation();
            r.setUtilisateur(Session.getCurrentUser());
            r.setVehicule(vehicule);
            r.setDateDebut(dateDebut.getValue());
            r.setDateFin(dateFin.getValue());

            long days = dateDebut.getValue().until(dateFin.getValue()).getDays();
            r.setMontantTotal(days * vehicule.getPrixJournalier());

            reservationService.reserver(r);
            System.out.println("Réservation confirmée");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
