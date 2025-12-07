package model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class Reservation {
    private int id;
    private Utilisateur utilisateur;
    private Vehicule vehicule;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String status;
    private double montantTotal;
}
