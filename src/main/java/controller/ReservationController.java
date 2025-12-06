package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationController {

    @FXML
    private TableView<Reservation> tableReservations;

    @FXML
    private TableColumn<Reservation, Integer> colId;

    @FXML
    private TableColumn<Reservation, Utilisateur> colUtilisateur;

    @FXML
    private TableColumn<Reservation, Vehicule> colVehicule;

    @FXML
    private TableColumn<Reservation, LocalDateTime> colDateDebut;

    @FXML
    private TableColumn<Reservation, LocalDateTime> colDateFin;

    @FXML
    private TableColumn<Reservation, String> colStatus;

    @FXML
    private TableColumn<Reservation, Double> colMontant;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Colonnes objets : afficher des attributs des objets
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        colUtilisateur.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNom() + " " + item.getPrenom());
            }
        });

        colVehicule.setCellValueFactory(new PropertyValueFactory<>("vehicule"));
        colVehicule.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Vehicule item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMarque() + " " + item.getModele());
            }
        });

        // Colonnes LocalDateTime formatées
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateDebut.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });

        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colDateFin.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });

        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));

        // Ajouter des données fictives
        initData();

        tableReservations.setItems(reservationsList);

        // Boutons
        btnAjouter.setOnAction(e -> ajouterReservation());
        btnModifier.setOnAction(e -> modifierReservation());
        btnSupprimer.setOnAction(e -> supprimerReservation());
    }

    private void initData() {
        // Utilisateurs
        Utilisateur user1 = new Utilisateur();
        user1.setId(1);
        user1.setNom("Alice");
        user1.setPrenom("Dupont");

        Utilisateur user2 = new Utilisateur();
        user2.setId(2);
        user2.setNom("Bob");
        user2.setPrenom("Martin");

        // Véhicules
        Vehicule veh1 = new Vehicule();
        veh1.setId(1);
        veh1.setMarque("Toyota");
        veh1.setModele("Corolla");

        Vehicule veh2 = new Vehicule();
        veh2.setId(2);
        veh2.setMarque("Renault");
        veh2.setModele("Clio");

        // Réservations
        Reservation res1 = new Reservation();
        res1.setId(1);
        res1.setUtilisateur(user1);
        res1.setVehicule(veh1);
        res1.setDateDebut(LocalDateTime.of(2025, 12, 1, 10, 0));
        res1.setDateFin(LocalDateTime.of(2025, 12, 5, 10, 0));
        res1.setStatus("Confirmée");
        res1.setMontantTotal(250.0);

        Reservation res2 = new Reservation();
        res2.setId(2);
        res2.setUtilisateur(user2);
        res2.setVehicule(veh2);
        res2.setDateDebut(LocalDateTime.of(2025, 12, 2, 14, 0));
        res2.setDateFin(LocalDateTime.of(2025, 12, 4, 14, 0));
        res2.setStatus("En attente");
        res2.setMontantTotal(120.0);

        reservationsList.addAll(res1, res2);
    }

    private void ajouterReservation() {
        System.out.println("Ajouter une réservation");
    }

    private void modifierReservation() {
        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Modifier la réservation : " + selected.getUtilisateur().getNom());
        }
    }

    private void supprimerReservation() {
        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Supprimer la réservation : " + selected.getUtilisateur().getNom());
            reservationsList.remove(selected);
        }
    }
}
