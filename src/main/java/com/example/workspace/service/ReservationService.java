package com.example.workspace.service;

import com.example.workspace.entity.*;
import com.example.workspace.repository.*;
import com.example.workspace.vue.ReservationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ReservationOptionRepository reservationOptionRepository;
    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;

    // Préparation de la réservation
    public String prepareReservation(Long workspaceId, String workspaceName, String selectedDate, Model model, HttpSession session) {

        List<Option> options = optionRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        Long clientId = (Long) session.getAttribute("clientId");
        Client client = clientRepository.getById(clientId);

        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("workspaceName", workspaceName);
        model.addAttribute("selectedDate", selectedDate);  // Ajouter la date sélectionnée au modèle
        model.addAttribute("options", options);
        model.addAttribute("clients", clients);
        model.addAttribute("numero", client.getNumber());
        model.addAttribute("idalefa", client.getId());

        return "reservation";  // Redirige vers la page de réservation
    }
    // Vérifier si la réservation dépasse l'heure de fermeture et doit être partagée
    private boolean isReservationSplitRequired(LocalTime startTime, int duration) {
        LocalTime endTime = startTime.plusHours(duration);
        // Si l'heure de fin dépasse 19h, la réservation doit être partagée
        return endTime.isAfter(LocalTime.of(19, 0));
    }

    private List<Reservation> splitReservation(Reservation originalReservation) {
        List<Reservation> splitReservations = new ArrayList<>();
        LocalTime originalEndTime = originalReservation.getStartHour().plusHours(originalReservation.getDuration());
        LocalTime splitEndTime = LocalTime.of(19, 0);

        // Si l'heure de fin dépasse 19h, il faut diviser la réservation
        while (originalEndTime.isAfter(splitEndTime)) {
            // Créer la première partie jusqu'à 19h
            Reservation firstPart = new Reservation(
                    originalReservation.getDate(),
                    originalReservation.getStartHour(),
                    (int) java.time.Duration.between(originalReservation.getStartHour(), splitEndTime).toHours(),
                    originalReservation.getWorkspace(),
                    originalReservation.getReserveur(),
                    originalReservation.getClient()
            );
            splitReservations.add(firstPart);  // Ajouter la première partie à la liste

            // Créer la seconde partie pour le lendemain matin à 8h
            LocalTime startSecondPart = LocalTime.of(8, 0);
            LocalTime endSecondPart = startSecondPart.plusHours(originalReservation.getDuration() - (int) java.time.Duration.between(originalReservation.getStartHour(), splitEndTime).toHours());

            // Si la durée restante est toujours plus de 19h, répéter le processus
            originalReservation = new Reservation(
                    originalReservation.getDate().plusDays(1),  // La date est décalée au lendemain
                    startSecondPart,
                    (int) java.time.Duration.between(startSecondPart, endSecondPart).toHours(),
                    originalReservation.getWorkspace(),
                    originalReservation.getReserveur(),
                    originalReservation.getClient()
            );
            originalEndTime = originalReservation.getStartHour().plusHours(originalReservation.getDuration()); // Recalcule l'heure de fin de la nouvelle réservation
        }

        // Ajouter la dernière partie si elle ne dépasse pas 19h
        if (originalEndTime.isBefore(splitEndTime)) {
            Reservation lastPart = new Reservation(
                    originalReservation.getDate(),
                    originalReservation.getStartHour(),
                    (int) java.time.Duration.between(originalReservation.getStartHour(), originalEndTime).toHours(),
                    originalReservation.getWorkspace(),
                    originalReservation.getReserveur(),
                    originalReservation.getClient()
            );
            splitReservations.add(lastPart);  // Ajouter la dernière partie
        }

        return splitReservations;  // Retourner toutes les réservations split
    }




    public String confirmReservation(Long workspaceId, Long reserveurId, String reservationDate, String startHour,
                                     int duration, Long[] optionIds, HttpSession session, Model model) {
        Long clientId = (Long) session.getAttribute("clientId");

        // Vérification si le client est connecté
        if (clientId == null) {
            return "redirect:/client/login";  // Redirige vers la page de connexion
        }

        try {
            // Conversion de la date et de l'heure de début
            LocalDate date = LocalDate.parse(reservationDate);
            LocalTime startTime = LocalTime.of(Integer.parseInt(startHour), 0);
            LocalTime endTime = startTime.plusHours(duration);

            // Vérifier la disponibilité du créneau
            if (!isSlotAvailable(date, startTime, endTime, workspaceId, model)) {
                return "erreurdereservation";
            }

            // Vérifier l'existence de l'espace de travail et des clients
            Long sessionClientId = (Long) session.getAttribute("clientId");
            if (!validateWorkspaceAndClients(workspaceId, sessionClientId, reserveurId, model)) {
                return "erreurdereservation";
            }

            // Vérifier si la réservation doit être partagée avant de créer la réservation
            if (isReservationSplitRequired(startTime, duration)) {
                // Afficher un message dans la console et arrêter l'exécution de la fonction
                System.out.println("Réservation doit être départagée. La fonction s'arrête.");

                // Créer la réservation (même si elle sera départagée ensuite)
                Reservation reservation = new Reservation(date, startTime, duration, workspaceRepository.getById(workspaceId),
                        clientRepository.getById(sessionClientId), clientRepository.getById(reserveurId));

                // Générer la référence de la réservation
                reservation.setRef(generateReservationReference());

                List<Reservation> splitReservations = splitReservation(reservation);  // Départager la réservation si nécessaire

                // Vérifier si tous les créneaux des réservations splittées sont disponibles
                if (areAllSlotsAvailable(splitReservations, date, workspaceId)) {
                    // Si tous les créneaux sont disponibles, sauvegarder toutes les réservations
                    reservationRepository.saveAll(splitReservations);

                    // Associer les options à chaque réservation splittée
                    if (optionIds != null && optionIds.length > 0) {
                        List<Option> options = optionRepository.findAllById(Arrays.asList(optionIds));
                        options.forEach(option -> {
                            splitReservations.forEach(res -> reservationOptionRepository.save(new ReservationOption(res, option)));
                        });
                    }

                    model.addAttribute("reussite", "Réservation réussie !");
                    System.out.println("nety le izy ");
                    return "reservationConfirmation";  // Affiche la confirmation après enregistrement
                } else {
                    // Si un créneau est déjà occupé, afficher l'erreur
                    model.addAttribute("Erreur", "Un ou plusieurs créneaux de réservation sont déjà occupés.");
                    return "erreurdereservation";  // Retourner l'erreur si chevauchement
                }
            }

            // Récupérer l'espace de travail, le client et le réservateur
            Workspace workspace = workspaceRepository.getById(workspaceId);
            Client client = clientRepository.getById(sessionClientId);
            Client reserveur = clientRepository.getById(reserveurId);

            // Créer et enregistrer la réservation
            String newRef = generateReservationReference();
            Reservation reservation = new Reservation(date, startTime, duration, workspace, reserveur, client);
            reservation.setRef(newRef);
            Reservation savedReservation = reservationRepository.save(reservation);

            // Associer les options à la réservation
            if (optionIds != null && optionIds.length > 0) {
                List<Option> options = optionRepository.findAllById(Arrays.asList(optionIds));
                options.forEach(option -> reservationOptionRepository.save(new ReservationOption(savedReservation, option)));
            }

            model.addAttribute("reussite", "Réservation réussie !");
            return "reservationConfirmation";

        } catch (Exception e) {
            model.addAttribute("Erreur", "Une erreur s'est produite. Veuillez réessayer.");
            return "erreurdereservation";
        }
    }

    private boolean areAllSlotsAvailable(List<Reservation> reservations, LocalDate date, Long workspaceId) {
        for (Reservation res : reservations) {
            // Vérifier si le créneau de la réservation est déjà pris
            List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                    date, res.getStartHour(), res.getEndHour(), workspaceId);
            if (!conflictingReservations.isEmpty()) {
                return false;  // Si un créneau est occupé, retourner false
            }
        }
        return true;  // Tous les créneaux sont libres
    }





    // Vérifier la disponibilité du créneau
    private boolean isSlotAvailable(LocalDate date, LocalTime startTime, LocalTime endTime, Long workspaceId, Model model) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(date, startTime, endTime, workspaceId);
        if (!conflictingReservations.isEmpty()) {
            model.addAttribute("Erreur", "Erreur de réservation, créneau indisponible.");
            return false;
        }
        return true;
    }

    // Vérifier l'existence de l'espace de travail et des clients
    private boolean validateWorkspaceAndClients(Long workspaceId, Long sessionClientId, Long reserveurId, Model model) {
        Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);
        Optional<Client> clientOpt = clientRepository.findById(sessionClientId);
        Optional<Client> reserveurOpt = clientRepository.findById(reserveurId);

        if (!workspaceOpt.isPresent() || !clientOpt.isPresent() || !reserveurOpt.isPresent()) {
            model.addAttribute("Erreur", "Espace de travail ou client non trouvé.");
            return false;
        }
        return true;
    }

    // Générer une nouvelle référence pour la réservation
    private String generateReservationReference() {
        String lastRef = findLastRef();
        return generateNextRef(lastRef);
    }

    // Générer la référence suivante
    private String generateNextRef(String lastRef) {
        if (lastRef == null || lastRef.isEmpty()) {
            return "r001";
        }
        String numPart = lastRef.substring(1);
        int nextNum = Integer.parseInt(numPart) + 1;
        return String.format("r%03d", nextNum);
    }

    // Trouver la dernière référence
    private String findLastRef() {
        Reservation lastReservation = reservationRepository.findTopByOrderByRefDesc();
        return lastReservation != null ? lastReservation.getRef() : null;
    }

    /* payment */
    // Fonction pour récupérer les informations de la réservation à payer
    public String getReservationToPay(Long idReservation, Model model) {
        // Récupération de la réservation et des détails de la réservation
        Reservation reservation = reservationRepository.findById(idReservation).orElse(null);
        ReservationDetails reservationDetails = reservationDetailsRepository.findById(idReservation).orElse(null);

        // Vérifier si la réservation et les détails existent
        if (reservation == null || reservationDetails == null) {
            model.addAttribute("Erreur", "Réservation ou détails non trouvés.");
            return "erreurdereservation";
        }

        // Ajout des informations au modèle pour la vue
        model.addAttribute("reservation", reservation);
        model.addAttribute("reservationDetails", reservationDetails);

        return "payerReservation";  // Redirige vers la page de paiement
    }

    /***********************/

    // Fonction pour traiter le paiement d'une réservation
    public String payerviareference(Long idReservation, String mode) {
        // Récupération de la réservation
        Reservation reservation = reservationRepository.findById(idReservation).orElse(null);

        // Vérification si la réservation existe
        if (reservation != null) {
            // Mise à jour du statut de la réservation en "En attente"
            reservation.setStatus(ReservationStatus.EN_ATTENTE);
            reservationRepository.save(reservation);  // Sauvegarder la réservation mise à jour
        }

        return "traitementPayerReservation";  // Retourne la vue pour le traitement du paiement
    }

    /*******************************************************************************************************/
    // Fonction pour traiter le paiement d'une réservation
    public String processPayment(Long idReservation, String mode,HttpSession session , Model model) {
        // Récupération de la réservation
        Reservation reservation = reservationRepository.findById(idReservation).orElse(null);

        // Vérification si la réservation existe
        if (reservation != null) {
            // Mise à jour du statut de la réservation en "En attente"
            reservation.setStatus(ReservationStatus.EN_ATTENTE);
            reservationRepository.save(reservation);  // Sauvegarder la réservation mise à jour

            // Création et configuration du paiement
            Payment payment = new Payment();
            payment.setMode_payment(mode);
            payment.setReservation(reservation);
            payment.setRef_payment(Payment.generateRef());
            payment.setRef_reservation(reservation.getRef());
            payment.setStatut(String.valueOf(reservation.getStatus()));

            // Sauvegarder le paiement
            paymentRepository.save(payment);
        }

        return "huhu";  // Retourne la vue après le traitement
    }
}
