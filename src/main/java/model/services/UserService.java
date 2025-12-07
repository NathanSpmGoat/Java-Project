package model.services;

import model.dao.UtilisateurDAO;
import model.entities.Utilisateur;

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

    public int createUser(Utilisateur user) throws SQLException {
        // vérifier email unique
        if (utilisateurDAO.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        return utilisateurDAO.add(user);
    }

    public boolean updateUser(Utilisateur user) throws SQLException {
        // Par ex. interdire modification d'email si collision, etc.
        Utilisateur existing = utilisateurDAO.findByEmail(user.getEmail());
        if (existing != null && existing.getId() != user.getId()) {
            throw new IllegalArgumentException("Email déjà associé à un autre compte");
        }
        return utilisateurDAO.update(user);
    }

    public boolean deleteUser(int userId) throws SQLException {
        // Ici, on pourrait vérifier qu'il n'existe pas de réservations actives, etc.
        return utilisateurDAO.delete(userId);
    }

    public Utilisateur findById(int id) throws SQLException {
        return utilisateurDAO.findById(id);
    }

    public List<Utilisateur> getAllUsers() throws SQLException {
        return utilisateurDAO.getAll();
    }
}
