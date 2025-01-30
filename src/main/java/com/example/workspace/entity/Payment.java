package com.example.workspace.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Random;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Ref_payment;
    private LocalDate date_payment;
    private String mode_payment;
    private String statut;  // "EN_ATTENTE", "PAYE", etc.

    // Nouvelle colonne pour stocker la référence de la réservation
    private String ref_reservation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id") // Cette colonne stockera l'ID de la réservation
    private Reservation reservation;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef_payment() {
        return Ref_payment;
    }

    public void setRef_payment(String ref_payment) {
        Ref_payment = ref_payment;
    }

    public LocalDate getDate_payment() {
        return date_payment;
    }

    public void setDate_payment(LocalDate date_payment) {
        this.date_payment = date_payment;
    }

    public String getMode_payment() {
        return mode_payment;
    }

    public void setMode_payment(String mode_payment) {
        this.mode_payment = mode_payment;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (reservation != null) {
            this.ref_reservation = reservation.getRef(); // Met à jour automatiquement ref_reservation
        }
    }

    public String getRef_reservation() {
        return ref_reservation;
    }

    public void setRef_reservation(String ref_reservation) {
        this.ref_reservation = ref_reservation;
    }

    public static String generateRef() {
        Random random = new Random();

        // Générer un nombre entre 0 et 1 pour choisir le format
        boolean isNumeric = random.nextBoolean();

        if (isNumeric) {
            // Générer un numéro à 6 chiffres (ex: 190029)
            return String.format("%06d", random.nextInt(1000000));
        } else {
            // Générer une référence alphanumérique (ex: R14355)
            char letter = (char) ('A' + random.nextInt(26)); // Lettre entre A et Z
            int number = 10000 + random.nextInt(90000); // Nombre entre 10000 et 99999
            return letter + String.valueOf(number);
        }
    }

}
