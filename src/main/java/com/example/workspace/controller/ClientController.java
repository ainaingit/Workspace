package com.example.workspace.controller;

import com.example.workspace.entity.Option;
import com.example.workspace.entity.Reservation;
import com.example.workspace.entity.Workspace;
import com.example.workspace.repository.OptionRepository;
import com.example.workspace.repository.ReservationRepository;
import com.example.workspace.repository.WorkspaceRepository;
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
                                     Model model) {

        // Conversion de la date en type LocalDate
        LocalDate date = LocalDate.parse(reservationDate);
        int startHourInt = Integer.parseInt(startHour);  // Convertir l'heure de début en entier

        // Créer une nouvelle réservation
        Reservation reservation = new Reservation();
        reservation.setWorkspace(workspaceRepository.findById(workspaceId).orElse(null));
        reservation.setDate(date);
        reservation.setStartHour(startHourInt);
        reservation.setDuration(duration);


        // Si des options ont été sélectionnées, les ajouter à la réservation
        if (optionIds != null && optionIds.length > 0) {

            for (int i = 0; i < optionIds.length; i++) {
                System.out.println(optionIds[i]);
            }
        }


        // Ajouter les informations à la vue ou rediriger vers une autre page
        model.addAttribute("reservation", reservation);
        return "reservationConfirmation";  // Vue de confirmation de réservation
    }


}
