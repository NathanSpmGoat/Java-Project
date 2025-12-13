package model.dao;

import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final VehiculeDAO vehiculeDAO = new VehiculeDAO();

    public int add(Reservation r) throws SQLException {
        String sql = "INSERT INTO reservation(utilisateur_id, vehicule_id, dateDebut, dateFin, statut, montantTotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getUtilisateur().getId());
            ps.setInt(2, r.getVehicule().getId());
            ps.setDate(3, Date.valueOf(r.getDateDebut()));
            ps.setDate(4, Date.valueOf(r.getDateFin()));
            ps.setString(5, r.getStatut());
            ps.setDouble(6, r.getMontantTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public boolean update(Reservation r) throws SQLException {
        String sql = "UPDATE reservation SET utilisateur_id=?, vehicule_id=?, dateDebut=?, dateFin=?, statut=?, montantTotal=? WHERE id=?";
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

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

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

    public int countOverlaps(int vehiculeId, LocalDate debut, LocalDate fin) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation "
                + "WHERE vehicule_id=? AND statut <> ? AND (dateDebut < ? AND dateFin > ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculeId);
            ps.setString(2, "annulée");
            ps.setDate(3, Date.valueOf(fin));
            ps.setDate(4, Date.valueOf(debut));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countOverlapsExcludingId(int vehiculeId, LocalDate debut, LocalDate fin, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation "
                + "WHERE vehicule_id=? AND statut <> ? AND id <> ? AND (dateDebut < ? AND dateFin > ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculeId);
            ps.setString(2, "annulée");
            ps.setInt(3, excludeId);
            ps.setDate(4, Date.valueOf(fin));
            ps.setDate(5, Date.valueOf(debut));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /** CHARGEMENT COMPLET UTILISATEUR + VEHICULE */
    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();

        r.setId(rs.getInt("id"));
        r.setDateDebut(rs.getDate("dateDebut").toLocalDate());
        r.setDateFin(rs.getDate("dateFin").toLocalDate());
        r.setStatut(rs.getString("statut"));
        r.setMontantTotal(rs.getDouble("montantTotal"));

        // Charger ENTITÉS complètes
        int userId = rs.getInt("utilisateur_id");
        int vehId = rs.getInt("vehicule_id");

        r.setUtilisateur(utilisateurDAO.findById(userId));
        r.setVehicule(vehiculeDAO.findById(vehId));

        return r;
    }
}
