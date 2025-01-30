package com.example.workspace.entity;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Ref_payment ;
    private LocalDate date_payment ;
    private String mode_payment ;
    @OneToOne(cascade = CascadeType.ALL )
    @JoinColumn(name = "ref_reservation")
    private Reservation reservation ;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getRef_payment() {
        return Ref_payment;
    }

    public String getMode_payment() {
        return mode_payment;
    }

    public void setMode_payment(String mode_payment) {
        this.mode_payment = mode_payment;
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
