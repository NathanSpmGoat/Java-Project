package model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Vehicule {
    private int id;
    private String marque;
    private String modele;
    private String matricule;
    private String type;
    private double prixJournalier;
    private String etat;
}
