package model.dao;

import model.entities.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la table vehicule.
 * Fournit toutes les opérations CRUD + méthodes utilitaires.
 */
public class VehiculeDAO {

    /**
     * Ajout d’un véhicule
     */
    public int add(Vehicule v) throws SQLException {
        String sql = "INSERT INTO vehicule(marque, modele, type, prix_journalier, etat, matricule) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setString(3, v.getType());
            ps.setDouble(4, v.getPrixJournalier());
            ps.setString(5, v.getEtat());
            ps.setString(6, v.getMatricule()); // si tu l’as dans ton entité

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        }
    }

    /**
     * Mise à jour d’un véhicule
     */
    public boolean update(Vehicule v) throws SQLException {
        String sql = "UPDATE vehicule SET marque=?, modele=?, type=?, prix_journalier=?, etat=?, matricule=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setString(3, v.getType());
            ps.setDouble(4, v.getPrixJournalier());
            ps.setString(5, v.getEtat());
            ps.setString(6, v.getMatricule());
            ps.setInt(7, v.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Suppression
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM vehicule WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Recherche par id
     */
    public Vehicule findById(int id) throws SQLException {
        String sql = "SELECT * FROM vehicule WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToVehicule(rs);
            }
            return null;
        }
    }

    /**
     * Lister tous les véhicules
     */
    public List<Vehicule> getAll() throws SQLException {
        String sql = "SELECT * FROM vehicule";
        List<Vehicule> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowToVehicule(rs));
        }
        return list;
    }

    /**
     * Mise à jour de l’état (DISPONIBLE / LOUE / EN_PANNE...)
     */
    public boolean updateEtat(int id, String etat) throws SQLException {
        String sql = "UPDATE vehicule SET etat = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, etat);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * trouver les véhicules disponibles
     * Utilisable par ReservationService pour vérifier disponibilité
     */
    public List<Vehicule> findAvailable() throws SQLException {
        String sql = "SELECT * FROM vehicule WHERE etat = 'Disponible'";

        List<Vehicule> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowToVehicule(rs));
        }
        return list;
    }

    /**
     * Mapping ResultSet -> Objet Vehicule
     */
    private Vehicule mapRowToVehicule(ResultSet rs) throws SQLException {
        Vehicule v = new Vehicule();

        v.setId(rs.getInt("id"));
        v.setMarque(rs.getString("marque"));
        v.setModele(rs.getString("modele"));
        v.setType(rs.getString("type"));
        v.setPrixJournalier(rs.getDouble("prix_journalier"));
        v.setEtat(rs.getString("etat"));
        v.setMatricule(rs.getString("matricule"));

        return v;
    }
}
