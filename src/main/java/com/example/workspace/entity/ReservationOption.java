package com.example.workspace.entity;

import jakarta.persistence.*;

@Entity
public class ReservationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;  // La réservation associée

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;  // L'option choisie

    public ReservationOption() {}

    public ReservationOption(Reservation reservationId, Option byId) {
        this.setReservation(reservationId);
        this.setOption(byId);
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
