package model.services;

import model.dao.ReservationDAO;
import model.dao.VehiculeDAO;
import model.entities.Reservation;
import model.entities.Vehicule;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service regroupant la logique métier des réservations.
 */
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final VehiculeDAO vehiculeDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.vehiculeDAO = new VehiculeDAO();
    }

    public ReservationService(ReservationDAO reservationDAO, VehiculeDAO vehiculeDAO) {
        this.reservationDAO = reservationDAO;
        this.vehiculeDAO = vehiculeDAO;
    }

    /**
     * Crée une réservation en vérifiant:
     */
    public int reserver(Reservation r) throws SQLException {

        // Vérifications de base
        if (r.getDateDebut() == null || r.getDateFin() == null ||
                r.getVehicule() == null || r.getUtilisateur() == null) {
            throw new IllegalArgumentException("Données de réservation incomplètes");
        }

        if (!r.getDateDebut().isBefore(r.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }

        // Vérification de disponibilité
        int overlaps = reservationDAO.countOverlaps(r.getVehicule().getId(),
                r.getDateDebut(),
                r.getDateFin());
        if (overlaps > 0) {
            throw new IllegalStateException("Véhicule non disponible sur la période demandée");
        }

        // Calcul du montant total (en jours)
        double prixJour = fetchPrixJournalier(r.getVehicule().getId());
        long jours = ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin());
        if (jours <= 0) jours = 1;
        r.setMontantTotal(prixJour * jours);

        //  Statut par défaut (évite NULL dans la base)
        if (r.getStatut() == null || r.getStatut().isEmpty()) {
            r.setStatut("confirmée");
        }

        // Enregistrement BDD
        int newId = reservationDAO.add(r);

        // Mise à jour état véhicule
        vehiculeDAO.updateEtat(r.getVehicule().getId(), "RESERVE");

        return newId;
    }

    /**
     * Annuler une réservation si elle n'a pas encore commencé.
     */
    public boolean annulerReservation(int reservationId) throws SQLException {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) return false;

        if (!r.getDateDebut().isAfter(java.time.LocalDate.now())) {
            throw new IllegalStateException("Impossible d'annuler une réservation déjà commencée");
        }

        r.setStatut("annulée");
        boolean ok = reservationDAO.update(r);

        if (ok) vehiculeDAO.updateEtat(r.getVehicule().getId(), "DISPONIBLE");

        return ok;
    }

    /**
     * Modifier une réservation existante avec recalcul du montant.
     */
    public boolean modifierReservation(Reservation r) throws SQLException {

        if (!r.getDateDebut().isBefore(r.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }

        int overlaps = reservationDAO.countOverlaps(
                r.getVehicule().getId(),
                r.getDateDebut(),
                r.getDateFin()
        );

        if (overlaps > 0) {
            throw new IllegalStateException("Période non disponible");
        }

        long jours = ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin());
        double prixJour = fetchPrixJournalier(r.getVehicule().getId());
        r.setMontantTotal(prixJour * Math.max(1, jours));

        return reservationDAO.update(r);
    }

    public Reservation findById(int id) throws SQLException {
        return reservationDAO.findById(id);
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.getAll();
    }

    /**
     * Retourne le prix journalier du véhicule lié à la réservation.
     */
    private double fetchPrixJournalier(int vehiculeId) throws SQLException {
        Vehicule v = vehiculeDAO.findById(vehiculeId);
        if (v == null) throw new IllegalStateException("Véhicule introuvable");
        return v.getPrixJournalier();
    }
}
