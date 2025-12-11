package model.dao;

import model.entities.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la table reservation.
 */
public class ReservationDAO {

    /**
     * Ajoute une réservation et retourne l'id généré (ou -1 en cas d'erreur).
     */
    public int add(Reservation r) throws SQLException {

        // Les noms correspondent à ta table MySQL
        String sql = "INSERT INTO reservation(utilisateur_id, vehicule_id, dateDebut, dateFin, statut, montantTotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // ID utilisateur et véhicule
            ps.setInt(1, r.getUtilisateur().getId());
            ps.setInt(2, r.getVehicule().getId());

            // Conversion LocalDate -> java.sql.Date
            ps.setDate(3, Date.valueOf(r.getDateDebut()));
            ps.setDate(4, Date.valueOf(r.getDateFin()));

            // Statut et montant
            ps.setString(5, r.getStatut());
            ps.setDouble(6, r.getMontantTotal());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            // Récupération clé générée
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

            return -1;
        }
    }

    /**
     * Met à jour une réservation existante.
     */
    public boolean update(Reservation r) throws SQLException {

        String sql = "UPDATE reservation SET "
                + "utilisateur_id=?, vehicule_id=?, dateDebut=?, dateFin=?, statut=?, montantTotal=? "
                + "WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getUtilisateur().getId());
            ps.setInt(2, r.getVehicule().getId());
            ps.setDate(3, Date.valueOf(r.getDateDebut()));
            ps.setDate(4, Date.valueOf(r.getDateFin()));
            ps.setString(5, r.getStatut());
            ps.setDouble(6, r.getMontantTotal());
            ps.setInt(7, r.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Supprime une réservation par son ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Recherche une réservation par son ID.
     */
    public Reservation findById(int id) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRowToReservation(rs) : null;
            }
        }
    }

    /**
     * Retourne toutes les réservations de la base.
     */
    public List<Reservation> getAll() throws SQLException {
        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT * FROM reservation";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowToReservation(rs));
        }

        return list;
    }

    /**
     * Compte les chevauchements de dates pour un véhicule donné.
     *
     * Condition SQL équivalente à :
     *   existing.start < new.end AND existing.end > new.start
     *
     * Ignorer les réservations avec statut = 'annulée'.
     */
    public int countOverlaps(int vehiculeId, LocalDate debut, LocalDate fin) throws SQLException {

        String sql = "SELECT COUNT(*) FROM reservation "
                + "WHERE vehicule_id=? AND statut <> ? "
                + "AND (dateDebut < ? AND dateFin > ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculeId);
            ps.setString(2, "annulée");

            ps.setDate(3, Date.valueOf(fin));     // existing.dateDebut < new.fin
            ps.setDate(4, Date.valueOf(debut));   // existing.dateFin > new.debut

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Transforme une ligne SQL en objet Reservation.
     * Charge uniquement les IDs des objets liés (Utilisateur/Vehicule).
     */
    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {

        Reservation r = new Reservation();

        r.setId(rs.getInt("id"));

        // Charger uniquement les IDs liés
        model.entities.Utilisateur u = new model.entities.Utilisateur();
        u.setId(rs.getInt("utilisateur_id"));
        r.setUtilisateur(u);

        model.entities.Vehicule v = new model.entities.Vehicule();
        v.setId(rs.getInt("vehicule_id"));
        r.setVehicule(v);

        // Conversion SQL Date -> LocalDate
        r.setDateDebut(rs.getDate("dateDebut").toLocalDate());
        r.setDateFin(rs.getDate("dateFin").toLocalDate());

        // Champs simples
        r.setStatut(rs.getString("statut"));
        r.setMontantTotal(rs.getDouble("montantTotal"));

        return r;
    }
}
