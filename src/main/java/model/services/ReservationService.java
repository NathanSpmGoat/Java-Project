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

    /**
     * Crée une réservation (avec bénéficiaire si nécessaire)
     */
    public int reserver(Reservation r) throws SQLException {

        validateReservation(r);

        Utilisateur u = r.getUtilisateur();

        // Si un utilisateur est fourni, vérifier s'il existe déjà
        if (u != null) {
            Utilisateur existing = utilisateurDAO.findByEmail(u.getEmail());

            if (existing != null) {
                r.setUtilisateur(existing);
            } else {
                // Création d'un nouveau bénéficiaire
                u.setRole("BENEFICIAIRE");
                u.setMotDePasse("temp1234"); // mot de passe temporaire obligatoire pour MySQL
                int userId = utilisateurDAO.add(u);
                u.setId(userId);
                r.setUtilisateur(u);
            }
        }

        // Vérifier la disponibilité du véhicule
        int overlaps = reservationDAO.countOverlaps(
                r.getVehicule().getId(),
                r.getDateDebut(),
                r.getDateFin()
        );

        if (overlaps > 0)
            throw new IllegalStateException("Véhicule non disponible sur la période demandée");

        // Calcul du montant
        calculMontantTotal(r);

        if (r.getStatut() == null || r.getStatut().isEmpty())
            r.setStatut("confirmée");

        // Insertion réservation
        int newId = reservationDAO.add(r);

        // Mettre à jour l'état du véhicule
        vehiculeDAO.updateEtat(r.getVehicule().getId(), "RESERVE");

        return newId;
    }

    /** Modifier une réservation */
    public boolean modifierReservation(Reservation r) throws SQLException {

        validateReservation(r);

        int overlaps = reservationDAO.countOverlapsExcludingId(
                r.getVehicule().getId(),
                r.getDateDebut(),
                r.getDateFin(),
                r.getId()
        );

        if (overlaps > 0)
            throw new IllegalStateException("Période non disponible");

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

        if (ok)
            vehiculeDAO.updateEtat(r.getVehicule().getId(), "DISPONIBLE");

        return ok;
    }

    /** Récupérer toutes les réservations */
    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.getAll();
    }

    /** Récupérer tous les véhicules */
    public List<Vehicule> getAllVehicules() throws SQLException {
        return vehiculeDAO.getAll();
    }

    /** Récupérer tous les utilisateurs */
    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        return utilisateurDAO.getAll();
    }

    /** Validation simple */
    private void validateReservation(Reservation r) {

        if (r.getDateDebut() == null ||
                r.getDateFin() == null ||
                r.getVehicule() == null)
            throw new IllegalArgumentException("Données de réservation incomplètes");

        if (!r.getDateDebut().isBefore(r.getDateFin()))
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
    }

    /** Calcul du montant total */
    private void calculMontantTotal(Reservation r) throws SQLException {

        Vehicule v = vehiculeDAO.findById(r.getVehicule().getId());
        if (v == null)
            throw new IllegalStateException("Véhicule introuvable");

        long jours = ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin());
        if (jours <= 0) jours = 1;

        r.setMontantTotal(v.getPrixJournalier() * jours);
    }
}
