package model.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class Vehicule {
    private int id;
    private String marque;
    private String modele;
    private String type;
    private double PrixJournalier;
    private String etat;


}
