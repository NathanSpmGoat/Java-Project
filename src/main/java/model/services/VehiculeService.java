package model.services;

import model.dao.ReservationDAO;
import model.dao.VehiculeDAO;
import model.entities.Vehicule;

import java.sql.SQLException;
import java.util.List;

public class VehiculeService {

    // DAO utilisés pour gérer la persistence
    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;

    public VehiculeService() {
        this.vehiculeDAO = new VehiculeDAO();
        this.reservationDAO = new ReservationDAO();
    }

    public VehiculeService(VehiculeDAO vehiculeDAO, ReservationDAO reservationDAO) {
        this.vehiculeDAO = vehiculeDAO;
        this.reservationDAO = reservationDAO;
    }

    // Ajout avec validation minimum
    public int addVehicule(Vehicule v) throws SQLException {
        if (v.getMarque() == null || v.getModele() == null) {
            throw new IllegalArgumentException("Marque / modèle requis");
        }
        if (v.getMatricule() == null || v.getMatricule().isEmpty()) {
            throw new IllegalArgumentException("Matricule requis");
        }
        return vehiculeDAO.add(v);
    }

    // Mise à jour simple
    public boolean updateVehicule(Vehicule v) throws SQLException {
        return vehiculeDAO.update(v);
    }

    // Suppression sécurisée → impossible si réservation existante
    public boolean deleteVehicule(int id) throws SQLException {
        int overlaps = reservationDAO.countOverlaps(id, null, null);
        if (overlaps > 0)
            throw new IllegalStateException("Impossible de supprimer : réservations en cours");

        return vehiculeDAO.delete(id);
    }

    public Vehicule findById(int id) throws SQLException {
        return vehiculeDAO.findById(id);
    }

    public List<Vehicule> getAllVehicules() throws SQLException {
        return vehiculeDAO.getAll();
    }

    // Liste des véhicules non réservés
    public List<Vehicule> getAvailableVehicules() throws SQLException {
        return vehiculeDAO.findAvailable();
    }

    // Permet de changer l’état : DISPONIBLE / RESERVE
    public boolean setEtat(int vehiculeId, String etat) throws SQLException {
        return vehiculeDAO.updateEtat(vehiculeId, etat);
    }
}
