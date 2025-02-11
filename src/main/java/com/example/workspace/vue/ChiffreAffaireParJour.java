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

    @Column(name = "chiffre_affaire_payes", precision = 10, scale = 2)
    private BigDecimal chiffreAffairePayes;

    @Column(name = "chiffre_affaire_non_payes", precision = 10, scale = 2)
    private BigDecimal chiffreAffaireNonPayes;

    @Column(name = "chiffre_affaire_total", precision = 10, scale = 2)
    private BigDecimal chiffreAffaireTotal;

    // Constructeur par défaut
    public ChiffreAffaireParJour() {}

    // Constructeur avec paramètres
    public ChiffreAffaireParJour(LocalDate datePaiement, BigDecimal chiffreAffairePayes, BigDecimal chiffreAffaireNonPayes, BigDecimal chiffreAffaireTotal) {
        this.datePaiement = datePaiement;
        this.chiffreAffairePayes = chiffreAffairePayes;
        this.chiffreAffaireNonPayes = chiffreAffaireNonPayes;
        this.chiffreAffaireTotal = chiffreAffaireTotal;
    }

    // Getters et Setters
    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimal getChiffreAffairePayes() {
        return chiffreAffairePayes;
    }

    public void setChiffreAffairePayes(BigDecimal chiffreAffairePayes) {
        this.chiffreAffairePayes = chiffreAffairePayes;
    }

    public BigDecimal getChiffreAffaireNonPayes() {
        return chiffreAffaireNonPayes;
    }

    public void setChiffreAffaireNonPayes(BigDecimal chiffreAffaireNonPayes) {
        this.chiffreAffaireNonPayes = chiffreAffaireNonPayes;
    }

    public BigDecimal getChiffreAffaireTotal() {
        return chiffreAffaireTotal;
    }

    public void setChiffreAffaireTotal(BigDecimal chiffreAffaireTotal) {
        this.chiffreAffaireTotal = chiffreAffaireTotal;
    }

    @Override
    public String toString() {
        return "ChiffreAffaireParJour{" +
                "datePaiement=" + datePaiement +
                ", chiffreAffairePayes=" + chiffreAffairePayes +
                ", chiffreAffaireNonPayes=" + chiffreAffaireNonPayes +
                ", chiffreAffaireTotal=" + chiffreAffaireTotal +
                '}';
    }
}
