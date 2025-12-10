package model.dao;

import model.entities.Vehicule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {

    // Ajouter un véhicule
    public int add(Vehicule v) throws SQLException {
        String sql = "INSERT INTO vehicule(marque, modele, type, prixJournalier, etat, matricule) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setString(3, v.getType());
            ps.setDouble(4, v.getPrixJournalier());
            ps.setString(5, v.getEtat());
            ps.setString(6, v.getMatricule()); // ajout matricule

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        }
    }

    // Update
    public boolean update(Vehicule v) throws SQLException {
        String sql = "UPDATE vehicule SET marque=?, modele=?, type=?, prixJournalier=?, etat=?, matricule=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setString(3, v.getType());
            ps.setDouble(4, v.getPrixJournalier());
            ps.setString(5, v.getEtat());
            ps.setString(6, v.getMatricule()); // ajout matricule
            ps.setInt(7, v.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Modifier l'état d'un véhicule
    public boolean updateEtat(int vehiculeId, String etat) throws SQLException {
        String sql = "UPDATE vehicule SET etat=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, etat);
            ps.setInt(2, vehiculeId);

            return ps.executeUpdate() > 0;
        }
    }

    // Delete
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM vehicule WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Find by ID
    public Vehicule findById(int id) throws SQLException {
        String sql = "SELECT * FROM vehicule WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    // Lister TOUS les véhicules
    public List<Vehicule> getAll() throws SQLException {
        String sql = "SELECT * FROM vehicule";
        List<Vehicule> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // Véhicules disponibles
    public List<Vehicule> findAvailable() throws SQLException {
        String sql = "SELECT * FROM vehicule WHERE etat='DISPONIBLE'";
        List<Vehicule> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // Mapper ResultSet -> Vehicule
    private Vehicule map(ResultSet rs) throws SQLException {
        Vehicule v = new Vehicule();
        v.setId(rs.getInt("id"));
        v.setMarque(rs.getString("marque"));
        v.setModele(rs.getString("modele"));
        v.setType(rs.getString("type"));
        v.setPrixJournalier(rs.getDouble("prixJournalier"));
        v.setEtat(rs.getString("etat"));
        v.setMatricule(rs.getString("matricule")); // ajout matricule
        return v;
    }
}
