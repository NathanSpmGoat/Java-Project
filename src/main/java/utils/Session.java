package utils;

import lombok.Getter;
import lombok.Setter;
import model.entities.Utilisateur;

public class Session {

    @Getter
    @Setter
    private static Utilisateur currentUser;

    public static void clear() {
        currentUser = null;
    }
}
