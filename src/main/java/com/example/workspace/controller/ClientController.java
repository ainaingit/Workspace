package com.example.workspace.controller;

import com.example.workspace.entity.*;
import com.example.workspace.vue.ReservationDetails;
import com.example.workspace.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
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
    @Autowired
    private PaymentRepository paymentRepository;

    public String findLastRef() {
        Reservation lastReservation = reservationRepository.findTopByOrderByRefDesc();
        return lastReservation != null ? lastReservation.getRef() : null;
    }

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
        LocalTime startHourInt = LocalTime.of(Integer.parseInt(startHour), 0);  // Convertir l'heure de début en entier
        LocalTime endtime = startHourInt.plusHours(duration);

        // Récupérer la session du client
        Long sessionclient = (Long) session.getAttribute("clientId");

        // Récupérer les objets workspace et client
        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        Client client = clientRepository.findById(sessionclient).orElse(null);

        // Vérifier s'il y a des conflits de créneau
        boolean isAvailable = true;
        if (workspace != null && client != null && !reservationRepository.findConflictingReservations(date, startHourInt, endtime).isEmpty()) {
            isAvailable = false;
        }

        // Logique si il n'y a pas de conflit
        if (isAvailable) {
            // Récupérer la dernière réservation (ou un modèle de référence si aucune réservation)
            String lastRef = findLastRef();
            String newRef = generateNextRef(lastRef);

            // Créer la réservation avec la nouvelle référence
            Reservation reservation = new Reservation(date, startHourInt, duration, workspace, client);
            reservation.setRef(newRef);  // Set la nouvelle référence pour la réservation

            // Sauvegarder la réservation en base de données
            reservation = reservationRepository.save(reservation);  // L'ID sera généré automatiquement

            // Ajouter les options à la réservation
            for (Long optionId : optionIds) {
                Option option = optionRepository.findById(optionId).orElse(null);
                if (option != null) {
                    ReservationOption resopt = new ReservationOption(reservation, option);
                    reservationOptionRepository.save(resopt);
                }
            }

            return "reservationConfirmation";  // Vue de confirmation de réservation
        } else {
            model.addAttribute("Erreur", "Erreur de réservation, créneau indisponible.");
            return "reservation";  // Vue de la page de réservation avec l'erreur
        }
    }


    private String generateNextRef(String lastRef) {
        if (lastRef == null || lastRef.isEmpty()) {
            return "ref001";  // Si aucune référence précédente, commencer avec "ref001"
        }
        // Extraire le numéro de la dernière référence (par exemple, "ref003" devient "003")
        String numPart = lastRef.substring(3);
        int nextNum = Integer.parseInt(numPart) + 1;  // Incrémenter le numéro
        return String.format("ref%03d", nextNum);  // Retourner la nouvelle référence sous forme "ref004"
    }


    @PostMapping("/annulerReservation")
    public String annulerReservation( @RequestParam("reservationId") Long idReservation,
                                       Model model) {
        reservationRepository.deleteById(idReservation);
        System.out.println("Reservation deleted");
        return "annulerReservation";
    }

    @PostMapping("/payerReservation")
    public String payerReservation( @RequestParam("reservationId") Long idReservation,
                                      Model model) {
        Reservation reservation = reservationRepository.findById(idReservation).get();
        ReservationDetails reservationDetails = reservationDetailsRepository.findById(idReservation).get();
        model.addAttribute("reservation", reservation);
        model.addAttribute("reservationDetails", reservationDetails);
        return "payerReservation";
    }

    @PostMapping("/traitementPayerReservation")
    public String payerunereservation(@RequestParam("mode") String mode, @RequestParam("reservationId") Long idres) {
        // Charger la réservation par son ID
        Reservation reservation = reservationRepository.findById(idres).orElse(null);

        if (reservation != null) {
            // Mettre à jour le statut de la réservation
            reservation.setStatus(ReservationStatus.EN_ATTENTE);

            // Sauvegarder la réservation mise à jour
            reservationRepository.save(reservation);

        }

        return "traitementPayerReservation";
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

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("reservationId")Long id ,
                                 @RequestParam("mode") String mode,
                                 HttpSession session,
                                 Model model) {
        Reservation reservation = reservationRepository.findById(id).get();
        reservation.setStatus(ReservationStatus.EN_ATTENTE);
        System.out.println("tonga ato re ah ");
        Payment payment = new Payment();
        payment.setMode_payment(mode);
        payment.setReservation(reservation);
        payment.setRef_payment(Payment.generateRef());
        payment.setRef_reservation(reservation.getRef());
        payment.setStatut(String.valueOf(reservation.getStatus()));

        paymentRepository.save(payment) ;
        return "huhu";
    }
}
