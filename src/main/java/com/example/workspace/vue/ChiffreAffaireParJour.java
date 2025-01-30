package com.example.workspace.vue;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "v_chiffre_affaire_par_jour") // Nom de la vue en base
public class ChiffreAffaireParJour {

    @Id
    @Column(name = "date_paiement") // Clé primaire basée sur la date
    private LocalDate datePaiement;

    @Column(name = "chiffre_affaire", precision = 10, scale = 2)
    private BigDecimal chiffreAffaire;

    // Constructeur par défaut
    public ChiffreAffaireParJour() {}

    // Constructeur avec paramètres
    public ChiffreAffaireParJour(LocalDate datePaiement, BigDecimal chiffreAffaire) {
        this.datePaiement = datePaiement;
        this.chiffreAffaire = chiffreAffaire;
    }

    // Getters et Setters
    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimal getChiffreAffaire() {
        return chiffreAffaire;
    }

    public void setChiffreAffaire(BigDecimal chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }

    @Override
    public String toString() {
        return "ChiffreAffaireParJour{" +
                "datePaiement=" + datePaiement +
                ", chiffreAffaire=" + chiffreAffaire +
                '}';
    }
}
