package com.example.workspace.controller;

import com.example.workspace.entity.*;
import com.example.workspace.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImportCSV {
    @Autowired
    private  ClientRepository clientRepository ;

    @Autowired
    private OptionRepository optionRepository ;

    @Autowired
    private WorkspaceRepository workspaceRepository ;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationOptionRepository reservationOptionRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @PostMapping("/import")
    public String ImporterCSV(
            @RequestParam("optionalItemsFile") MultipartFile optionFile,
            @RequestParam("workspaceFile") MultipartFile workspaceFile,
            @RequestParam("reservationClientFile") MultipartFile reservationFile,
            @RequestParam("PaymentFile") MultipartFile paymentFile) {

        try {

            // Prend les clients
            List<Client> clients = Client.parseCSV(reservationFile.getInputStream());
            for (int i = 0; i < clients.size(); i++) {
                System.out.println("Client potentiel : " + clients.get(i).getNumber());
                // Vérifier si le client existe déjà dans la base de données
                if (clientRepository.findByNumber(clients.get(i).getNumber()) == null) {
                    clientRepository.save(clients.get(i));
                    System.out.println("✅ Client enregistré : " + clients.get(i).getNumber());
                } else {
                    System.out.println("⚠️ Client déjà existant, non enregistré : " + clients.get(i).getNumber());
                }
            }
            System.out.println("\n");

            // Traiter le fichier des options
            InputStream optionInputStream = optionFile.getInputStream();
            List<Option> options = Option.parseCSV(optionInputStream);
            System.out.println("Afficher les options :");

            for (Option option : options) {
                // Vérifier si l'option existe déjà dans la base
                Option existingOption = optionRepository.findByCode(option.getCode());
                if (existingOption == null) {
                    optionRepository.save(option);
                    System.out.println("Option enregistrée : " + option.getName());
                } else {
                    System.out.println("⚠️ Option déjà existante, non enregistrée : " + option.getName());
                }
            }
            System.out.println("\n");

            // Traiter le fichier des espaces de travail
            InputStream workspaceInputStream = workspaceFile.getInputStream();
            List<Workspace> workspaces = Workspace.parseCSV(workspaceInputStream);
            System.out.println("Afficher les espaces de travail :");

            for (Workspace workspace : workspaces) {
                // Vérifier si l'espace de travail existe déjà dans la base
                Workspace existingWorkspace = workspaceRepository.findByName(workspace.getName());
                if (existingWorkspace == null) {
                    workspaceRepository.save(workspace);
                    System.out.println("Espace de travail enregistré : " + workspace.getName());
                } else {
                    System.out.println("⚠️ Espace de travail déjà existant, non enregistré : " + workspace.getName());
                }
            }
            System.out.println("\n");

            // Traiter le fichier des réservations
            InputStream reservationInputStream = reservationFile.getInputStream();
            List<Reservation> reservations = Reservation.parseCSV(reservationInputStream, workspaceRepository, clientRepository, optionRepository);
            System.out.println("A propos des réservations :");
            for (int i = 0; i < reservations.size(); i++) {
                System.out.println("Date : " + reservations.get(i).getDate());
                System.out.println("Référence : " + reservations.get(i).getRef());
                System.out.println("Heure début : " + reservations.get(i).getStartHour());
                System.out.println("Heure fin : " + reservations.get(i).getEndHour());
                System.out.println("Durée : " + reservations.get(i).getDuration());
                System.out.println("Statut : " + reservations.get(i).getStatus());
                System.out.println("Numéro du client : " + reservations.get(i).getClient().getId());
                System.out.println("Nom du workspace : " + reservations.get(i).getWorkspace().getName());
                System.out.println("Début des options :");

                Reservation res = reservationRepository.save(reservations.get(i));

                for (int j = 0; j < res.getOptions().size(); j++) {
                    res.getOptions().get(j).setReservation(res);
                    ReservationOption resop = res.getOptions().get(j);
                    reservationOptionRepository.save(resop);
                }
            }

            // Importer le fichier des paiements
            InputStream paymentInputStream = paymentFile.getInputStream();
            List<Payment> liste_payment = Payment.importCsv(paymentInputStream, reservationRepository);
            paymentRepository.saveAll(liste_payment);
            List<Reservation> listedeux = new ArrayList<>();
            List<Payment> listeovaina = new ArrayList<>();
            for (int i = 0; i < liste_payment.size(); i++) {
                listedeux.add(reservationRepository.findByRef(liste_payment.get(i).getRef_reservation()));
            }

            for (Reservation reservation : listedeux) {
                if (reservation != null) {
                    reservation.setStatus(ReservationStatus.PAYE);
                }
            }

            for (Payment payment : liste_payment) {
                payment.setStatut(String.valueOf(ReservationStatus.PAYE));
            }

            // Mise à jour des objets dans la base de données
            reservationRepository.saveAll(listedeux);
            paymentRepository.saveAll(liste_payment);

            System.out.println("ImporterCSV effectué");

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'importation des fichiers";
        }

        return "Importation réussie !";  // Ou la vue que vous souhaitez afficher après l'importation
    }

}
