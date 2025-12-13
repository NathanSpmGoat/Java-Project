package controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Reservation;
import model.entities.Vehicule;
import model.entities.Utilisateur;
import model.services.ReservationService;
import model.services.PdfGeneratorService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationFormController {

    @FXML private Label vehiculeLabel;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private Label joursLabel;
    @FXML private Label montantLabel;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private Button btnConfirmer;
    @FXML private Button btnAnnuler;

    private Vehicule vehicule;
    private final ReservationService reservationService = new ReservationService();

    // Méthode appelée depuis HomeUtilisateurController
    public void setVehicule(Vehicule v) {
        this.vehicule = v;
        vehiculeLabel.setText("Réserver le véhicule : " + v.getMarque() + " " + v.getModele());
    }

    @FXML
    public void initialize() {
        // Calcul dynamique lorsque les dates changent
        ChangeListener<LocalDate> dateListener = (obs, oldDate, newDate) -> updateCalcul();
        dateDebutPicker.valueProperty().addListener(dateListener);
        dateFinPicker.valueProperty().addListener(dateListener);

        btnConfirmer.setOnAction(e -> confirmerReservation());
        btnAnnuler.setOnAction(e -> annuler());
    }

    private void updateCalcul() {
        LocalDate debut = dateDebutPicker.getValue();
        LocalDate fin = dateFinPicker.getValue();

        if (debut != null && fin != null && (debut.isBefore(fin) || debut.equals(fin))) {
            long jours = ChronoUnit.DAYS.between(debut, fin);
            if (jours == 0) jours = 1; // minimum 1 jour
            joursLabel.setText(String.valueOf(jours));

            double montant = jours * vehicule.getPrixJournalier();
            montantLabel.setText(String.format("%.2f", montant));
        } else {
            joursLabel.setText("0");
            montantLabel.setText("0.0");
        }
    }

    private void confirmerReservation() {
        LocalDate debut = dateDebutPicker.getValue();
        LocalDate fin = dateFinPicker.getValue();

        if (debut == null || fin == null) {
            showAlert("Erreur", "Veuillez sélectionner les dates.");
            return;
        }
        if (fin.isBefore(debut)) {
            showAlert("Erreur", "La date de fin doit être après la date de début.");
            return;
        }

        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir le nom, prénom et email du bénéficiaire.");
            return;
        }

        try {
            Reservation r = new Reservation();
            r.setVehicule(vehicule);

            // Création de l'utilisateur bénéficiaire
            Utilisateur beneficiaire = new Utilisateur();
            beneficiaire.setNom(nom);
            beneficiaire.setPrenom(prenom);
            beneficiaire.setEmail(email);

            r.setUtilisateur(beneficiaire);
            r.setDateDebut(debut);
            r.setDateFin(fin);

            reservationService.reserver(r);

            // Génération PDF pour le bénéficiaire
            PdfGeneratorService.generateReservationPDF(
                    r,
                    beneficiaire,
                    vehicule
            );

            showAlert("Succès", "Réservation effectuée ! Le PDF a été généré.");
            closeWindow();

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Erreur", "Impossible de créer la réservation : " + ex.getMessage());
        } catch (IllegalStateException ex) {
            showAlert("Indisponible", ex.getMessage());
        }
    }

    private void annuler() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
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
