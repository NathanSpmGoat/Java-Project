package model.services;

import model.dao.UtilisateurDAO;
import model.entities.Utilisateur;
import utils.HashUtil;
import utils.Session;

import java.sql.SQLException;

/**
 * Service d'authentification.
 * - Ne contient pas d'UI, renvoie des objets ou booleans.
 * - Utilise UtilisateurDAO pour charger l'utilisateur par email.
 */
public class AuthService {

    private final UtilisateurDAO utilisateurDAO;

    public AuthService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    // Pour tests / injection
    public AuthService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    /**
     * Tente d'authentifier un utilisateur.
     * @param email email fourni
     * @param plainPassword mot de passe en clair fourni
     * @return l'objet Utilisateur si authentifié, sinon null
     * @throws SQLException si erreur BD
     */
    public Utilisateur login(String email, String plainPassword) throws SQLException {
        Utilisateur u = utilisateurDAO.findByEmail(email);
        if (u == null) return null;

        // HashUtil doit fournir : boolean verify(String plain, String hashed)
        boolean ok = HashUtil.verify(plainPassword, u.getMotDePasse());
        if (!ok) return null;

        // Mettre en session l'utilisateur (classe utils.Session)
        Session.setCurrentUser(u);
        return u;
    }

    /**
     * Déconnecte l'utilisateur courant.
     */
    public void logout() {
        Session.clear();
    }

    /**
     * Crée un nouvel utilisateur en hashant le mot de passe avant insertion.
     * Vérifie unicité email via DAO.
     */
    public boolean register(Utilisateur u, String plainPassword) throws SQLException {
        if (utilisateurDAO.findByEmail(u.getEmail()) != null) {
            return false; // email déjà pris
        }
        // HashUtil.hash retourne la chaîne hashée (bcrypt/argon2 attendu)
        String hashed = HashUtil.hash(plainPassword);
        u.setMotDePasse(hashed);
        return utilisateurDAO.add(u) > 0;
    }
}
