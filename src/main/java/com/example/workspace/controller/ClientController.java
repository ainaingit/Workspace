package com.example.workspace.controller;

import com.example.workspace.entity.*;
import com.example.workspace.repository.*;
import com.example.workspace.service.ReservationService;
import com.example.workspace.vue.ReservationDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;
    @Autowired
    private ReservationService reservationService;

    private String findLastRef() {
        Reservation lastReservation = reservationRepository.findTopByOrderByRefDesc();
        return lastReservation != null ? lastReservation.getRef() : null;
    }

    @PostMapping("/reserver")
    public String reserveWorkspace(@RequestParam("workspaceId") Long workspaceId,
                                   @RequestParam("workspaceName") String workspacename,
                                   @RequestParam("selectedDate") String selectedDate,  // Récupérer la date sélectionnée
                                   Model model,
                                   HttpSession session) {
        Long clientId = (Long) session.getAttribute("clientId");
        if (clientId == null) {
            return "redirect:/client/login";  // Redirige vers la page de login si clientId est null
        }

        // Affichage de la date sélectionnée
        System.out.println("Date sélectionnée : " + selectedDate);

        // Appel de la méthode prepareReservation avec la date sélectionnée
        return reservationService.prepareReservation(workspaceId, workspacename, selectedDate, model, session);
    }


    @PostMapping("/confirmReservation")
    public String confirmReservation(@RequestParam("workspaceId") Long workspaceId,
                                     @RequestParam("reserveurId") Long reserveurId,
                                     @RequestParam("reservationDate") String reservationDate,
                                     @RequestParam("startHour") String startHour,
                                     @RequestParam("duration") int duration,
                                     @RequestParam(value = "option", required = false) Long[] optionIds,
                                     HttpSession session,
                                     Model model) {
        Long clientId = (Long) session.getAttribute("clientId");
        if (clientId == null) {
            return "redirect:/client/login";  // Redirige vers la page de login si clientId est null
        }
        return reservationService.confirmReservation(workspaceId, reserveurId, reservationDate, startHour, duration, optionIds, session, model);
    }



    @PostMapping("/annulerReservation")
    public String annulerReservation(@RequestParam("reservationId") Long idReservation, HttpSession session, Model model) {
        // Supprimer la réservation
        reservationRepository.deleteById(idReservation);

        // Ajouter un message de confirmation dans le modèle (facultatif)
        model.addAttribute("message", "La réservation avec l'ID " + idReservation + " a été supprimée.");

        // Rediriger vers la page des réservations de l'utilisateur
        return "redirect:/mesreservation";
    }



    @PostMapping("/payerReservation")
    public String payerReservation(@RequestParam("reservationId") Long idReservation, Model model) {
        // Appeler la méthode du service pour obtenir les informations de la réservation
        return reservationService.getReservationToPay(idReservation, model);
    }


    @PostMapping("/traitementPayerReservation")
    public String payerunereservation(@RequestParam("mode") String mode, @RequestParam("reservationId") Long idres) {
        // Appeler la méthode du service pour traiter le paiement
        return reservationService.payerviareference(idres, mode);
    }

    @GetMapping("/mesreservation")
    public String mesreservation(HttpSession session, Model model) {
        Long clientId = (Long) session.getAttribute("clientId");
        if (clientId == null) {
            return "redirect:/client/login";  // Redirige vers la page de login si clientId est null
        }
        List<ReservationDetails> reservations = reservationDetailsRepository.findByClientId(clientId);
        model.addAttribute("reservations", reservations);
        return "mesreservations";
    }

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("reservationId") Long id,
                                 @RequestParam("mode") String mode,
                                 HttpSession session,
                                 Model model) {
        Long clientId = (Long) session.getAttribute("clientId");
        if (clientId == null) {
            return "redirect:/client/login";  // Redirige vers la page de login si clientId est null
        }
        return reservationService.processPayment(id, mode, session, model);
    }

}
