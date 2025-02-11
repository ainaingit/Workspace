package com.example.workspace.vue;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "divise_hours")
@Immutable // Pour indiquer que cette vue est en lecture seule
public class DiviseHours {

    @Id
    private String hour_slot; // Par exemple "08:00", "09:00", etc.

    private int reservations_count;
    private int rank; // Nouveau champ pour le classement

    // Getters et Setters
    public String getHour_slot() {
        return hour_slot;
    }

    public void setHour_slot(String hour_slot) {
        this.hour_slot = hour_slot;
    }

    public int getReservations_count() {
        return reservations_count;
    }

    public void setReservations_count(int reservations_count) {
        this.reservations_count = reservations_count;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
