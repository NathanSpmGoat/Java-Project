package model.services;

import model.dao.ReservationDAO;
import model.dao.VehiculeDAO;
import model.dao.UtilisateurDAO;
import model.entities.Reservation;
import model.entities.Vehicule;
import model.entities.Utilisateur;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final VehiculeDAO vehiculeDAO;
    private final UtilisateurDAO utilisateurDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.vehiculeDAO = new VehiculeDAO();
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public ReservationService(ReservationDAO reservationDAO, VehiculeDAO vehiculeDAO, UtilisateurDAO utilisateurDAO) {
        this.reservationDAO = reservationDAO;
        this.vehiculeDAO = vehiculeDAO;
        this.utilisateurDAO = utilisateurDAO;
    }

    /** Crée une réservation */
    public int reserver(Reservation r) throws SQLException {
        validateReservation(r);

        int overlaps = reservationDAO.countOverlaps(r.getVehicule().getId(), r.getDateDebut(), r.getDateFin());
        if (overlaps > 0) throw new IllegalStateException("Véhicule non disponible sur la période demandée");

        calculMontantTotal(r);

        if (r.getStatut() == null || r.getStatut().isEmpty()) r.setStatut("confirmée");

        int newId = reservationDAO.add(r);

        vehiculeDAO.updateEtat(r.getVehicule().getId(), "RESERVE");
        return newId;
    }

    /** Modifier une réservation */
    public boolean modifierReservation(Reservation r) throws SQLException {
        validateReservation(r);

        int overlaps = reservationDAO.countOverlapsExcludingId(r.getVehicule().getId(), r.getDateDebut(), r.getDateFin(), r.getId());
        if (overlaps > 0) throw new IllegalStateException("Période non disponible");

        calculMontantTotal(r);
        return reservationDAO.update(r);
    }

    /** Annuler une réservation */
    public boolean annulerReservation(int reservationId) throws SQLException {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) return false;

        if (!r.getDateDebut().isAfter(java.time.LocalDate.now()))
            throw new IllegalStateException("Impossible d'annuler une réservation déjà commencée");

        r.setStatut("annulée");
        boolean ok = reservationDAO.update(r);
        if (ok) vehiculeDAO.updateEtat(r.getVehicule().getId(), "DISPONIBLE");

        return ok;
    }

    /** Récupère une réservation par ID */
    public Reservation findById(int id) throws SQLException {
        return reservationDAO.findById(id);
    }

    /** Récupère toutes les réservations */
    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.getAll();
    }

    /** Récupère tous les véhicules */
    public List<Vehicule> getAllVehicules() throws SQLException {
        return vehiculeDAO.getAll();
    }

    /** Récupère tous les utilisateurs */
    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        return utilisateurDAO.getAll();
    }

    /** Validation basique */
    private void validateReservation(Reservation r) {
        if (r.getDateDebut() == null || r.getDateFin() == null ||
                r.getVehicule() == null || r.getUtilisateur() == null)
            throw new IllegalArgumentException("Données de réservation incomplètes");

        if (!r.getDateDebut().isBefore(r.getDateFin()))
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
    }

    /** Calcule le montant total */
    private void calculMontantTotal(Reservation r) throws SQLException {
        double prixJour = fetchPrixJournalier(r.getVehicule().getId());
        long jours = ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin());
        if (jours <= 0) jours = 1;
        r.setMontantTotal(prixJour * jours);
    }

    /** Prix journalier */
    private double fetchPrixJournalier(int vehiculeId) throws SQLException {
        Vehicule v = vehiculeDAO.findById(vehiculeId);
        if (v == null) throw new IllegalStateException("Véhicule introuvable");
        return v.getPrixJournalier();
    }
}
