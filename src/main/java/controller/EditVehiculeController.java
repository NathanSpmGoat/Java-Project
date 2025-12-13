package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Vehicule;
import model.services.VehiculeService;

public class EditVehiculeController {

    @FXML private TextField txtMarque;
    @FXML private TextField txtModele;
    @FXML private TextField txtMatricule;
    @FXML private TextField txtType;
    @FXML private TextField txtPrix;
    @FXML private ComboBox<String> comboEtat;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private final VehiculeService vehiculeService = new VehiculeService();
    private Vehicule selectedVehicule;

    /**
     * Passe le véhicule à éditer et pré-remplit les champs.
     */
    public void setVehicule(Vehicule v) {
        this.selectedVehicule = v;

        txtMarque.setText(v.getMarque());
        txtModele.setText(v.getModele());
        txtMatricule.setText(v.getMatricule());
        txtType.setText(v.getType());
        txtPrix.setText(String.valueOf(v.getPrixJournalier()));
        comboEtat.setValue(v.getEtat());
    }

    @FXML
    private void initialize() {
        comboEtat.setItems(FXCollections.observableArrayList("DISPONIBLE", "RESERVE"));

        btnModifier.setOnAction(e -> modifierVehicule());
        btnAnnuler.setOnAction(e -> fermerFenetre());
    }

    private void modifierVehicule() {
        try {
            if (selectedVehicule == null) {
                showAlert("Erreur", "Aucun véhicule sélectionné.");
                return;
            }

            selectedVehicule.setMarque(txtMarque.getText().trim());
            selectedVehicule.setModele(txtModele.getText().trim());
            selectedVehicule.setMatricule(txtMatricule.getText().trim());
            selectedVehicule.setType(txtType.getText().trim());
            selectedVehicule.setPrixJournalier(Double.parseDouble(txtPrix.getText()));
            selectedVehicule.setEtat(comboEtat.getValue());

            boolean success = vehiculeService.updateVehicule(selectedVehicule);
            if (success) {
                showAlert("Succès", "Véhicule modifié !");
                fermerFenetre();
            } else {
                showAlert("Erreur", "Échec de la modification.");
            }

        } catch (NumberFormatException ex) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (Exception ex) {
            showAlert("Erreur", ex.getMessage());
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
