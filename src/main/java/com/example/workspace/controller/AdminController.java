package com.example.workspace.controller;

import com.example.workspace.entity.Payment;
import com.example.workspace.entity.Reservation;
import com.example.workspace.entity.ReservationStatus;
import com.example.workspace.repository.*;
import com.example.workspace.vue.ChiffreAffaireParJour;
import com.example.workspace.vue.ChiffreAffaireTotal;
import com.example.workspace.vue.DiviseHours;
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

    @Autowired
    private DiviseHoursRepository diviseHoursRepository;

    @GetMapping("/importerdonnees")
    public String importerdonnees(){
        return "importerdonnées";
    }

    @GetMapping("/admin/statistiques")
    public String statistiques(@RequestParam(required = false) LocalDate startDate, Model model) {
        List<ChiffreAffaireParJour> listeChiffreAffaire = (startDate != null)
                ? chiffreAffaireRepository.findByDatePaiement(startDate)
                : chiffreAffaireRepository.findAll();

        List<ChiffreAffaireTotal> statistiques = chiffreAffaireTotalRepository.findAll();
        model.addAttribute("chiffredaffaire", statistiques.get(0));
        model.addAttribute("listechiffreaffaire", listeChiffreAffaire);

        return "statistique";
    }

    @GetMapping("/admin/paiement")
    public String loadpaiement(Model model) {
        List<Payment> listePayment = paymentRepository.findAll();
        model.addAttribute("listePayment", listePayment);
        return "liste_paiement";
    }

    @PostMapping("/admin/validerPaiement")
    public String validerPaiement(@RequestParam("paymentId") Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        Reservation reservation = payment.getReservation();

        reservation.setStatus(ReservationStatus.PAYE);
        payment.setDate_payment(LocalDate.now());
        payment.setStatut(String.valueOf(ReservationStatus.PAYE));

        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return "paiementValide";
    }


    @GetMapping("/admin/divise-hours")
    public String showDiviseHours(Model model) {
        model.addAttribute("diviseHours", diviseHoursRepository.findAllTopHours());
        return "divise_hours";
    }

}
