package model.services;

import model.dao.ReservationDAO;
import model.dao.VehiculeDAO;
import model.entities.Vehicule;

import java.sql.SQLException;
import java.util.List;

/**
 * Service métier des véhicules.
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
        if (v.getMarque() == null || v.getModele() == null) {
            throw new IllegalArgumentException("Marque / modèle requis");
        }
        return vehiculeDAO.add(v);
    }

    public boolean updateVehicule(Vehicule v) throws SQLException {
        return vehiculeDAO.update(v);
    }

    public boolean deleteVehicule(int id) throws SQLException {
        int overlaps = reservationDAO.countOverlaps(id, null, null);
        if (overlaps > 0) throw new IllegalStateException("Impossible de supprimer : réservations en cours");
        return vehiculeDAO.delete(id);
    }

    public Vehicule findById(int id) throws SQLException {
        return vehiculeDAO.findById(id);
    }

    public List<Vehicule> getAllVehicules() throws SQLException {
        return vehiculeDAO.getAll();
    }

    public List<Vehicule> getAvailableVehicules() throws SQLException {
        return vehiculeDAO.findAvailable();
    }

    public boolean setEtat(int vehiculeId, String etat) throws SQLException {
        return vehiculeDAO.updateEtat(vehiculeId, etat);
    }
}
