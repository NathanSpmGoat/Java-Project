package model.services;

import model.dao.UtilisateurDAO;
import model.entities.Utilisateur;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 * Service pour la gestion des utilisateurs (logique métier).
 */
public class UserService {

    private final UtilisateurDAO utilisateurDAO;

    public UserService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public UserService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    public int createUser(Utilisateur user) throws SQLException, NoSuchAlgorithmException {
        // vérifier email unique
        if (utilisateurDAO.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        // hacher le mot de passe avant insertion
        user.setMotDePasse(hashPassword(user.getMotDePasse()));
        return utilisateurDAO.add(user);
    }

    public boolean updateUser(Utilisateur user) throws SQLException, NoSuchAlgorithmException {
        // interdire modification d'email si collision
        Utilisateur existing = utilisateurDAO.findByEmail(user.getEmail());
        if (existing != null && existing.getId() != user.getId()) {
            throw new IllegalArgumentException("Email déjà associé à un autre compte");
        }
        // mot de passe déjà haché avant appel si nécessaire
        return utilisateurDAO.update(user);
    }

    public boolean deleteUser(int userId) throws SQLException {
        return utilisateurDAO.delete(userId);
    }

    public Utilisateur findById(int id) throws SQLException {
        return utilisateurDAO.findById(id);
    }

    public List<Utilisateur> getAllUsers() throws SQLException {
        return utilisateurDAO.getAll();
    }

    /**
     * Hachage SHA-256 d'un mot de passe
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
