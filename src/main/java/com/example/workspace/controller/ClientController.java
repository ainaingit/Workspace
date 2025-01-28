package com.example.workspace.controller;

import com.example.workspace.entity.*;
import com.example.workspace.objet.ReservationDetails;
import com.example.workspace.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationOptionRepository reservationOptionRepository;

    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;
    @PostMapping("/reserver")
    public String reserveWorkspace(@RequestParam("workspaceId") Long workspaceId,
                                   @RequestParam("workspaceName") String workspacename,
                                   Model model) {
        List<Option> options = optionRepository.findAll();
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("workspaceName", workspacename);
        model.addAttribute("options", options);
        return "reservation";  // Redirige vers la page des espaces de travail
    }

    @PostMapping("/confirmReservation")
    public String confirmReservation(@RequestParam("workspaceId") Long workspaceId,
                                     @RequestParam("reservationDate") String reservationDate,  // Date sous forme de String
                                     @RequestParam("startHour") String startHour,  // Heure sous forme de String
                                     @RequestParam("duration") int duration,  // Durée sous forme d'entier
                                     @RequestParam("option") Long[] optionIds,  // Tableau d'ID d'options sélectionnées
                                     HttpSession session,
                                     Model model) {

        // Conversion de la date en type LocalDate
        LocalDate date = LocalDate.parse(reservationDate);
        int startHourInt = Integer.parseInt(startHour);  // Convertir l'heure de début en entier

        // prendre la session du client
        Long sessionclient = (Long) session.getAttribute("clientId");

        // prendre les workspace et le client
        Workspace workspace = workspaceRepository.findById(workspaceId).get();
        Client client  = clientRepository.findById(sessionclient).get();

        // create a reservation
        Reservation reservation = new Reservation(date,startHourInt,duration,workspace,client);
        // Sauvegarder la réservation en base de données
        reservation = reservationRepository.save(reservation);  // Ici, l'ID sera généré automatiquement

        // Récupérer l'ID généré pour la réservation
        Long reservationId = reservation.getId();
        System.out.println("Reservation ID: " + reservationId);

        for (Long optionId : optionIds) {
            ReservationOption resopt = new ReservationOption(reservation,optionRepository.findById(optionId).get());
            reservationOptionRepository.save(resopt);
        }

        System.out.println("Reservayion et Option reservation inserer ");
        return "reservationConfirmation";  // Vue de confirmation de réservation
    }

    @GetMapping("/mesreservation")
    public String mesreservation( HttpSession session, Model model) {
        Long idclient  = (Long) session.getAttribute("clientId");
        System.out.println(idclient + " huhu ");
        List<ReservationDetails> reservations = reservationDetailsRepository.findByClientId(idclient);
        System.out.println(reservations.size() + " reservations");
        model.addAttribute("reservations", reservations);
        return "mesreservations";
    }
}
