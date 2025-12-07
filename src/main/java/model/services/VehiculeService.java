package model.services;

import model.dao.ReservationDAO;
import model.dao.VehiculeDAO;
import model.entities.Vehicule;

import java.sql.SQLException;
import java.util.List;

/**
 * Service pour la logique métier liée aux véhicules.
 */
public class VehiculeService {

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

    public int addVehicule(Vehicule v) throws SQLException {
        // vérifier unicité du matricule si tu as une colonne matricule
        if (v.getMarque() == null || v.getModele() == null) {
            throw new IllegalArgumentException("Marque / modèle requis");
        }
        // si add renvoie id
        return vehiculeDAO.add(v);
    }

    public boolean updateVehicule(Vehicule v) throws SQLException {
        return vehiculeDAO.update(v);
    }

    public boolean deleteVehicule(int id) throws SQLException {
        // Condition de suppression : pas de réservations actives sur ce véhicule
        int overlaps = reservationDAO.countOverlaps(id, /* période large */ null, null);
        // NOTE: si countOverlaps ne supporte pas null, tu dois avoir une DAO method checkActiveReservations(vehicleId)
        if (overlaps > 0) {
            throw new IllegalStateException("Impossible de supprimer : réservations en cours");
        }
        return vehiculeDAO.delete(id);
    }

    public Vehicule findById(int id) throws SQLException {
        return vehiculeDAO.findById(id);
    }

    public List<Vehicule> getAllVehicules() throws SQLException {
        return vehiculeDAO.getAll();
    }

    public List<Vehicule> getAvailableVehicules() throws SQLException {
        return vehiculeDAO.findAvailable(); // méthode à implémenter dans VehiculeDAO
    }

    public boolean setEtat(int vehiculeId, String etat) throws SQLException {
        return vehiculeDAO.updateEtat(vehiculeId, etat);
    }
}
