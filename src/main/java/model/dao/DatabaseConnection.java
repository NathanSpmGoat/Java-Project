package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour centraliser la connexion à la base de données.
 * Modifie l'URL, l'utilisateur et le mot de passe selon ta configuration MySQL.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/location_voiture?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // change ceci en prod

    // Empêcher l'instanciation
    private DatabaseConnection() {}

    /**
     * Retourne une nouvelle connexion JDBC. L'appelant doit fermer la connexion.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
