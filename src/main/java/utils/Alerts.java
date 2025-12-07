package utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utilitaires pour afficher des dialogues/alertes JavaFX de façon centralisée.
 * Toutes les méthodes sont statiques : appeler directement Alerts.showInfo("...").

 thread non-UI, les dialogues seront exécutés via Platform.runLater(...)
 */
public final class Alerts {

    private Alerts() { }

    /**
     * Affiche une alerte d'information simple.
     */
    public static void showInfo(String title, String message) {
        runOnFxThread(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }

    /**
     * Affiche une alerte de warning (avertissement).
     */
    public static void showWarning(String title, String message) {
        runOnFxThread(() -> {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }

    /**
     * Affiche une alerte d'erreur.
     */
    public static void showError(String title, String message) {
        runOnFxThread(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);
            a.showAndWait();
        });
    }

    /**
     * Affiche une boîte de confirmation.
     * Retourne true si l'utilisateur confirme (OK / Yes), false sinon.
     */
    public static boolean showConfirm(String title, String message) {
        final boolean[] result = {false};
        Runnable task = () -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(message);

            // Optionnel : renommer les boutons
            ButtonType yes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
            ButtonType no = new ButtonType("Non", ButtonBar.ButtonData.NO);
            a.getButtonTypes().setAll(yes, no);

            Optional<ButtonType> opt = a.showAndWait();
            result[0] = opt.isPresent() && opt.get() == yes;
        };

        if (Platform.isFxApplicationThread()) {
            task.run();
            return result[0];
        } else {
            final Object lock = new Object();
            Platform.runLater(() -> {
                try { task.run(); }
                finally {
                    synchronized (lock) { lock.notify(); }
                }
            });
            // attendre la fin - bloquant depuis un thread non-UI (utiliser avec prudence)
            synchronized (lock) {
                try { lock.wait(); } catch (InterruptedException ignored) {}
            }
            return result[0];
        }
    }

    /**
     * Affiche un message d'erreur avec exception (utile pour debugging).
     */
    public static void showException(String title, String header, String details) {
        runOnFxThread(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle(title);
            a.setHeaderText(header);
            a.setContentText(details);
            a.showAndWait();
        });
    }

    // Exécution de la tâche sur le thread JavaFX si nécessaire
    private static void runOnFxThread(Runnable r) {
        if (Platform.isFxApplicationThread()) r.run();
        else Platform.runLater(r);
    }
}
