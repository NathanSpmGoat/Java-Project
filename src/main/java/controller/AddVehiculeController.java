package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entities.Vehicule;
import model.services.VehiculeService;

public class AddVehiculeController {

    @FXML private TextField txtMarque;
    @FXML private TextField txtModele;
    @FXML private TextField txtMatricule;
    @FXML private TextField txtType;
    @FXML private TextField txtPrix;
    @FXML private ComboBox<String> comboEtat;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;

    private final VehiculeService vehiculeService = new VehiculeService();

    @FXML
    private void initialize() {
        // États possibles
        comboEtat.setItems(FXCollections.observableArrayList("DISPONIBLE", "RESERVE"));
        comboEtat.setValue("DISPONIBLE"); // par défaut

        // Actions des boutons
        btnAjouter.setOnAction(e -> ajouterVehicule());
        btnAnnuler.setOnAction(e -> fermerFenetre());
    }

    private void ajouterVehicule() {
        try {
            // Vérifications simples
            if (txtMarque.getText().isEmpty() || txtModele.getText().isEmpty() ||
                    txtMatricule.getText().isEmpty() || txtPrix.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs obligatoires doivent être remplis !");
                return;
            }

            double prix = Double.parseDouble(txtPrix.getText());

            Vehicule v = new Vehicule();
            v.setMarque(txtMarque.getText().trim());
            v.setModele(txtModele.getText().trim());
            v.setMatricule(txtMatricule.getText().trim());
            v.setType(txtType.getText().trim());
            v.setPrixJournalier(prix);
            v.setEtat(comboEtat.getValue());

            int newId = vehiculeService.addVehicule(v);
            v.setId(newId);

            showAlert("Succès", "Véhicule ajouté !");
            fermerFenetre();

        } catch (NumberFormatException ex) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (Exception ex) {
            showAlert("Erreur", "Impossible d'ajouter : " + ex.getMessage());
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
