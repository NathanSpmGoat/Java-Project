package model.dao;

import model.entities.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la table reservation.
 * - L'entité Reservation utilise LocalDate (date sans heure).
 * - Le DAO utilise java.sql.Date (Date.valueOf(LocalDate)) pour les opérations SQL.
 * - countOverlaps vérifie les chevauchements sur la plage [date_debut, date_fin).
 */
public class ReservationDAO {

    /**
     * Ajoute une réservation et retourne l'id généré (ou -1 si échec).
     */
    public int add(Reservation r) throws SQLException {
        String sql = "INSERT INTO reservation(utilisateur_id, vehicule_id, date_debut, date_fin, statut, montant_total) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getUtilisateur().getId());
            ps.setInt(2, r.getVehicule().getId());

            // Utiliser java.sql.Date pour LocalDate (date sans heure)
            LocalDate dDeb = r.getDateDebut();
            LocalDate dFin = r.getDateFin();
            ps.setDate(3, dDeb != null ? Date.valueOf(dDeb) : null);
            ps.setDate(4, dFin != null ? Date.valueOf(dFin) : null);

            ps.setString(5, r.getStatus());
            ps.setDouble(6, r.getMontantTotal());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
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
        String sql = "UPDATE reservation SET utilisateur_id = ?, vehicule_id = ?, date_debut = ?, date_fin = ?, statut = ?, montant_total = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getUtilisateur().getId());
            ps.setInt(2, r.getVehicule().getId());

            ps.setDate(3, r.getDateDebut() != null ? Date.valueOf(r.getDateDebut()) : null);
            ps.setDate(4, r.getDateFin() != null ? Date.valueOf(r.getDateFin()) : null);

            ps.setString(5, r.getStatus());
            ps.setDouble(6, r.getMontantTotal());
            ps.setInt(7, r.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Supprime une réservation par id.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Récupère une réservation par id.
     * Note : ici on ne joint pas Utilisateur/Vehicule ; on ne charge que leur id.
     * Les services peuvent appeler UtilisateurDAO.findById / VehiculeDAO.findById si besoin.
     */
    public Reservation findById(int id) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToReservation(rs);
                return null;
            }
        }
    }

    /**
     * Retourne toutes les réservations (mapping simple).
     * Si tu veux charger les objets Utilisateur/Vehicule, faire des JOIN ou appeler les DAO correspondants.
     */
    public List<Reservation> getAll() throws SQLException {
        String sql = "SELECT * FROM reservation";
        List<Reservation> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToReservation(rs));
        }
        return list;
    }

    /**
     * Compte le nombre de réservations qui CHEVALENT la période [debut, fin) pour un véhicule donné.
     *
     * Condition de chevauchement (A=[a1,a2), B=[b1,b2)) : a1 < b2 AND b1 < a2
     * La requête SQL utilise les dates stockées en 'date' (sans heure).
     */
    public int countOverlaps(int vehiculeId, LocalDate debut, LocalDate fin) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation " +
                "WHERE vehicule_id = ? AND statut <> ? " +
                "AND (date_debut < ? AND date_fin > ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehiculeId);

            // Standardiser la valeur de statut qui représente une annulation dans la BDD
            ps.setString(2, "annulée"); // -> si tu préfères, utilise "ANNULEE" sans accent en base

            // existing.date_debut < new.fin  AND existing.date_fin > new.debut
            ps.setDate(3, Date.valueOf(fin));   // existing.date_debut < new.fin
            ps.setDate(4, Date.valueOf(debut)); // existing.date_fin > new.debut

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return 0;
            }
        }
    }

    /**
     * Mappe ResultSet -> Reservation (charge uniquement les ids des objets liés).
     */
    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getInt("id"));

        // On ne charge que l'id pour les entités associées (service/DAO peuvent charger les objets complets)
        model.entities.Utilisateur u = new model.entities.Utilisateur();
        u.setId(rs.getInt("utilisateur_id"));
        r.setUtilisateur(u);

        model.entities.Vehicule v = new model.entities.Vehicule();
        v.setId(rs.getInt("vehicule_id"));
        r.setVehicule(v);

        java.sql.Date sqlDeb = rs.getDate("date_debut");
        java.sql.Date sqlFin = rs.getDate("date_fin");
        if (sqlDeb != null) r.setDateDebut(sqlDeb.toLocalDate());
        if (sqlFin != null) r.setDateFin(sqlFin.toLocalDate());

        r.setStatus(rs.getString("statut"));
        r.setMontantTotal(rs.getDouble("montant_total"));
        return r;
    }
}
