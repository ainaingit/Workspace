package com.example.workspace.model;

import com.example.workspace.entity.Reservation;
import com.example.workspace.entity.ReservationStatus;
import com.example.workspace.entity.Workspace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardModel {
    private List<Workspace> listeWorkspace;
    private List<Reservation> reservations;
    private Map<Long, Map<Integer, String>> cellStyles;
    private Map<Long, Map<Integer, String>> hourSymbols; // Stocke les symboles pour chaque heure

    // Définition des couleurs comme constantes statiques
    private static final String COULEUR_DEFAULT = "bg-warning";
    private static final String COULEUR_RESERVATION_NON_PAYEE = "bg-purple";
    private static final String COULEUR_RESERVATION_PAYEE = "bg-danger";
    private static final String SYMBOLE_RESERVATION = "❌";

    public DashboardModel(List<Workspace> listeWorkspace, List<Reservation> reservations) {
        this.listeWorkspace = listeWorkspace;
        this.reservations = reservations;
        this.cellStyles = new HashMap<>();
        this.hourSymbols = new HashMap<>();
        initializeCellStylesAndSymbols();
    }

    private void initializeCellStylesAndSymbols() {
        for (Workspace espace : listeWorkspace) {
            // Initialiser les Maps pour les styles et symboles
            Map<Integer, String> hourStyles = new HashMap<>();
            Map<Integer, String> hourSymbols = new HashMap<>();

            // Appeler la méthode pour remplir les styles et symboles
            populateStylesAndSymbols(espace, hourStyles, hourSymbols);

            // Stocker les styles et symboles dans les maps globales
            this.cellStyles.put(espace.getId(), hourStyles);
            this.hourSymbols.put(espace.getId(), hourSymbols);
        }
    }

    private void populateStylesAndSymbols(Workspace espace, Map<Integer, String> hourStyles, Map<Integer, String> hourSymbols) {
        for (int hour = 8; hour <= 18; hour++) {
            // Initialiser les valeurs par défaut
            hourStyles.put(hour, COULEUR_DEFAULT);
            hourSymbols.put(hour, "");

            // Vérifier les réservations pour chaque heure
            for (Reservation res : reservations) {
                if (res.getWorkspace().getId().equals(espace.getId()) &&
                        hour >= res.getStartHour().getHour() && hour < res.getStartHour().getHour() + res.getDuration()) {

                    // Déterminer la couleur et ajouter le symbole
                    hourStyles.put(hour, res.getStatus() == ReservationStatus.PAYE ? COULEUR_RESERVATION_PAYEE : COULEUR_RESERVATION_NON_PAYEE);
                    if (!res.getClient().getId().equals(res.getReserveur().getId())) {
                        hourSymbols.put(hour, SYMBOLE_RESERVATION);
                    }
                    break; // Arrêter dès qu'on trouve une réservation
                }
            }
        }
    }

    // Getters et setters
    public List<Workspace> getListeWorkspace() {
        return listeWorkspace;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public Map<Long, Map<Integer, String>> getCellStyles() {
        return cellStyles;
    }

    public Map<Long, Map<Integer, String>> getHourSymbols() {
        return hourSymbols;
    }
}
