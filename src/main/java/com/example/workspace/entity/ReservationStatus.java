package com.example.workspace.entity;

public enum ReservationStatus {
    FAIT,       // La réservation est terminée
    PAYE,       // La réservation a été payée
    A_PAYER,    // La réservation doit être payée
    EN_ATTENTE  // La réservation est en attente de confirmation
}
