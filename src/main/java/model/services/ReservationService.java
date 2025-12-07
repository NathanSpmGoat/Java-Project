package model.services;

import model.dao.ReservationDAO;
import model.dao.VehiculeDAO;
import model.entities.Reservation;
import model.entities.Vehicule;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service pour la logique métier des réservations.
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
     * Tente de créer une réservation.
     * - Vérifie que les dates sont valides
     * - Vérifie la disponibilité via reservationDAO.countOverlaps(...)
     * - Calcule le montant
     * - Insère la réservation et marque le véhicule si besoin
     * @return id de la réservation insérée, -1 si échec
     */
    public int reserver(Reservation r) throws SQLException {
        if (r.getDateDebut() == null || r.getDateFin() == null || r.getVehicule() == null || r.getUtilisateur() == null) {
            throw new IllegalArgumentException("Données de réservation incomplètes");
        }
        if (!r.getDateDebut().isBefore(r.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }

        // Vérifier disponibilité : countOverlaps attend LocalDate (selon ta DAO)
        int overlaps = reservationDAO.countOverlaps(r.getVehicule().getId(), r.getDateDebut(), r.getDateFin());
        if (overlaps > 0) {
            throw new IllegalStateException("Véhicule non disponible sur la période demandée");
        }

        // Calcul du montant
        double prixJour = fetchPrixJournalier(r.getVehicule().getId());
        long jours = ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin());
        if (jours <= 0) jours = 1; // sécurité
        double montant = prixJour * jours;
        r.setMontantTotal(montant);

        int newId = reservationDAO.add(r);

        // Optionnel : mettre à jour l'état du véhicule si tu veux l'immédiatement marquer "RESERVE"
        vehiculeDAO.updateEtat(r.getVehicule().getId(), "RESERVE");

        return newId;
    }

    /**
     * Annule la réservation si elle n'a pas encore commencé.
     */
    public boolean annulerReservation(int reservationId) throws SQLException {
        Reservation r = reservationDAO.findById(reservationId);
        if (r == null) return false;
        // si dateDebut is after today -> on peut annuler
        if (!r.getDateDebut().isAfter(java.time.LocalDate.now())) {
            throw new IllegalStateException("Impossible d'annuler une réservation déjà commencée ou passée");
        }
        r.setStatus("annulée");
        boolean ok = reservationDAO.update(r);
        // si ok, on peut changer l'état du véhicule (vérifier s'il n'y a pas d'autres réservations actives)
        // Simple approche : remettre DISPONIBLE
        if (ok) vehiculeDAO.updateEtat(r.getVehicule().getId(), "DISPONIBLE");
        return ok;
    }

    public boolean modifierReservation(Reservation r) throws SQLException {
        // logique : vérifier nouvelles dates si modifiées -> disponibilité
        if (!r.getDateDebut().isBefore(r.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }
        int overlaps = reservationDAO.countOverlaps(r.getVehicule().getId(), r.getDateDebut(), r.getDateFin());
        // attention : countOverlaps compte l'entité elle-même si déjà persistée ; ajoute logique pour exclure l'ID courant si besoin
        if (overlaps > 0) {
            throw new IllegalStateException("Période non disponible");
        }
        // recalcul montant
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

    private double fetchPrixJournalier(int vehiculeId) throws SQLException {
        Vehicule v = vehiculeDAO.findById(vehiculeId);
        if (v == null) throw new IllegalStateException("Véhicule introuvable");
        return v.getPrixJournalier();
    }
}
