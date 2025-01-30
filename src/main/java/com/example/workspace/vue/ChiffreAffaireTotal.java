package com.example.workspace.vue;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vue_chiffre_affaire_total")
public class ChiffreAffaireTotal {

    @Id
    @Column(name = "montant_paye")
    private Double montantPaye;

    @Column(name = "montant_a_payer")
    private Double montantAPayer;

    @Column(name = "chiffre_affaire_total")
    private Double chiffreAffaireTotal;

    // Getters et setters
    public Double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(Double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public Double getMontantAPayer() {
        return montantAPayer;
    }

    public void setMontantAPayer(Double montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

    public Double getChiffreAffaireTotal() {
        return chiffreAffaireTotal;
    }

    public void setChiffreAffaireTotal(Double chiffreAffaireTotal) {
        this.chiffreAffaireTotal = chiffreAffaireTotal;
    }
}