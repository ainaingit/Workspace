package com.example.workspace.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;  // Date de la réservation (date sans heure)

    private int startHour;   // Heure de début de la réservation (en entier, ex: 8 pour 8h)
    private int endHour;   // Heure de début de la réservation (en entier, ex: 8 pour 8h)
    private int duration;     // Durée de la réservation en heures

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;  // Statut de la réservation (fait, payé, à payer, en attente)

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;  // L'espace de travail réservé

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;  // Le client qui a effectué la réservation

    public Reservation() {}

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Calcul de l'heure de fin en fonction de l'heure de début et de la durée
    public int getEndHour() {
        return startHour + duration;
    }

    public Reservation(LocalDate date, int startHour, int duration, Workspace workspace, Client client) {
        this.setDate(date);
        this.setStartHour(startHour);
        this.setEndHour(startHour+duration);
        this.setDuration(duration);
        this.setStatus(ReservationStatus.valueOf("A_PAYER"));
        this.setWorkspace(workspace);
        this.setClient(client);
    }
}
