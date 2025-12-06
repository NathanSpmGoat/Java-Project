package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Vehicule;

public class VehiculeController {

    @FXML
    private TableView<Vehicule> tableVehicules;

    @FXML
    private TableColumn<Vehicule, Integer> colId;

    @FXML
    private TableColumn<Vehicule, String> colMarque;

    @FXML
    private TableColumn<Vehicule, String> colModele;

    @FXML
    private TableColumn<Vehicule, String> colType;

    @FXML
    private TableColumn<Vehicule, Double> colPrixJournalier;

    @FXML
    private TableColumn<Vehicule, String> colEtat;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixJournalier.setCellValueFactory(new PropertyValueFactory<>("PrixJournalier"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Ajouter des données fictives pour le moment
        vehiculesList.add(new Vehicule() {{
            setId(1);
            setMarque("Toyota");
            setModele("Corolla");
            setType("Berline");
            setPrixJournalier(50.0);
            setEtat("Disponible");
        }});
        vehiculesList.add(new Vehicule() {{
            setId(2);
            setMarque("Renault");
            setModele("Clio");
            setType("Citadine");
            setPrixJournalier(40.0);
            setEtat("Loué");
        }});
        vehiculesList.add(new Vehicule() {{
            setId(3);
            setMarque("Peugeot");
            setModele("308");
            setType("Berline");
            setPrixJournalier(55.0);
            setEtat("Disponible");
        }});

        tableVehicules.setItems(vehiculesList);

        // Gestion des clics sur les boutons
        btnAjouter.setOnAction(event -> ajouterVehicule());
        btnModifier.setOnAction(event -> modifierVehicule());
        btnSupprimer.setOnAction(event -> supprimerVehicule());
    }

    // Méthodes pour les boutons (placeholders)
    private void ajouterVehicule() {
        System.out.println("Ajouter un véhicule");
        // Plus tard : ouvrir un formulaire pour ajouter
    }

    private void modifierVehicule() {
        Vehicule selected = tableVehicules.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Modifier le véhicule : " + selected.getMarque() + " " + selected.getModele());
            // Plus tard : ouvrir formulaire pour modifier
        }
    }

    private void supprimerVehicule() {
        Vehicule selected = tableVehicules.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Supprimer le véhicule : " + selected.getMarque() + " " + selected.getModele());
            vehiculesList.remove(selected);
            // Plus tard : supprimer dans la base
        }
    }
}
