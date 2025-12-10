package model.dao;

import model.entities.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la table utilisateur.
 * Contient toutes les opérations CRUD et quelques requêtes utilitaires.
 */
public class UtilisateurDAO {

    /**
     * Ajouter un nouvel utilisateur
     * @param u l'utilisateur à ajouter
     * @return l'ID généré ou -1 si erreur
     * @throws SQLException
     */
    public int add(Utilisateur u) throws SQLException {
        // Assurer que le rôle n'est pas null
        if (u.getRole() == null || u.getRole().isEmpty()) {
            u.setRole("UTILISATEUR"); // rôle par défaut
        }

        String sql = "INSERT INTO utilisateur(nom, prenom, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasse()); // hashé si nécessaire
            ps.setString(5, u.getRole());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        }
    }

    /**
     * Mettre à jour un utilisateur existant
     */
    public boolean update(Utilisateur u) throws SQLException {
        if (u.getRole() == null || u.getRole().isEmpty()) {
            u.setRole("UTILISATEUR");
        }

        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasse());
            ps.setString(5, u.getRole());
            ps.setInt(6, u.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Supprimer un utilisateur par ID
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Récupérer un utilisateur par ID
     */
    public Utilisateur findById(int id) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToUtilisateur(rs);
                return null;
            }
        }
    }

    /**
     * Récupérer un utilisateur par email
     */
    public Utilisateur findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToUtilisateur(rs);
                return null;
            }
        }
    }

    /**
     * Lister tous les utilisateurs
     */
    public List<Utilisateur> getAll() throws SQLException {
        String sql = "SELECT * FROM utilisateur";
        List<Utilisateur> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToUtilisateur(rs));
            }
        }
        return list;
    }

    /**
     * Mapper un ResultSet vers un objet Utilisateur
     */
    private Utilisateur mapRowToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("password")); // correspond à la colonne SQL
        u.setRole(rs.getString("role"));
        return u;
    }
}