package com.example.workspace.entity;

import com.example.workspace.repository.ReservationRepository;
import jakarta.persistence.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;

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

    // Fonction pour importer les données CSV et créer une liste de paiements
    public static List<Payment> importCsv(InputStream inputStream, ReservationRepository reservationRepository) {
        List<Payment> payments = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            // Passer la première ligne si elle contient des entêtes
            reader.readLine(); // Skip the header line

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 3) {
                    Payment payment = new Payment();
                    payment.setRef_payment(columns[0].trim());

                    // Convertir la date (assumons que le format de la date est dd/MM/yyyy)
                    payment.setDate_payment(LocalDate.parse(columns[2].trim(), formatter));

                    // On définit les autres champs (mode_payment, statut) comme souhaité
                    payment.setMode_payment("Non défini");  // Tu peux ajouter une logique ici si nécessaire
                    payment.setStatut("EN_ATTENTE");  // On définit le statut par défaut
                    payment.setRef_reservation(columns[1].trim());
                    payment.setReservation(reservationRepository.findByRef(columns[1].trim()));
                    // Ajouter à la liste
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payments;
    }

}
