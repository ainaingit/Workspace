package com.example.workspace.controller;

import com.example.workspace.entity.Payment;
import com.example.workspace.entity.Reservation;
import com.example.workspace.entity.ReservationStatus;
import com.example.workspace.repository.ChiffreAffaireTotalRepository;
import com.example.workspace.repository.PaymentRepository;
import com.example.workspace.repository.ReservationRepository;
import com.example.workspace.vue.ChiffreAffaireParJour;
import com.example.workspace.repository.ChiffreAffaireRepository;
import com.example.workspace.vue.ChiffreAffaireTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private ChiffreAffaireRepository chiffreAffaireRepository;

    @Autowired
    private ChiffreAffaireTotalRepository chiffreAffaireTotalRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/importerdonnees")
    public String importerdonnees(){
        return "importerdonnées";
    }

    @GetMapping("/admin/statistiques")
    public String statistiques(@RequestParam(required = false) LocalDate startDate,
                               @RequestParam(required = false) LocalDate endDate,
                               Model model) {
        List<ChiffreAffaireParJour> listeChiffreAffaire;

        if (startDate != null && endDate != null) {
            listeChiffreAffaire = chiffreAffaireRepository.findByDatePaiementBetween(startDate, endDate);
        } else {
            listeChiffreAffaire = chiffreAffaireRepository.findAll();  // Si pas de dates, récupérer toutes les données
        }
        List<ChiffreAffaireTotal> statistiques = chiffreAffaireTotalRepository.findAll();
        System.out.println("angezany = " + statistiques.size());
        model.addAttribute("chiffredaffaire", statistiques.get(0));
        model.addAttribute("listechiffreaffaire", listeChiffreAffaire);
        return "statistique";  // Renvoie la vue 'statistiques.jsp'
    }
    @GetMapping("/admin/paiement")
    public String loadpaiement(Model model) {
        List<Payment> listePayment = paymentRepository.findAll();
        model.addAttribute("listePayment", listePayment);
        return "liste_paiement";
    }
    /*@GetMapping("/validerPaiement")
    public String validerPaiement(@RequestParam("reservationId") Long reservationId) {
        // Récupérer la réservation
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation != null) {
            // Récupérer le paiement correspondant à la réservation et vérifier son statut
            Payment paiement = paymentRepository.findByReservation(reservation);

            if (paiement != null) {
                // Mettre à jour le statut du paiement à "PAYE"
                paiement.setStatut("PAYE");
                paymentRepository.save(paiement);

                // Mettre à jour le statut de la réservation à "PAYE"
                reservation.setStatus(ReservationStatus.PAYE);
                reservationRepository.save(reservation);
            }
        }

        return "paiementValide";  // Page ou redirection après validation du paiement
    }*/

}
