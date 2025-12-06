package model.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class Reservation {
    private int id;
    private Utilisateur utilisateur;
    private Vehicule vehicule;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String status;
    private double montantTotal;

}
