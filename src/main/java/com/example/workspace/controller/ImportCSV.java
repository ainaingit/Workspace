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
            // prends les clients
             List<Client> clients = Client.parseCSV(reservationFile.getInputStream());
            for (int i = 0; i < clients.size(); i++) {
                System.out.println("client potentiel  :" + clients.get(i).getNumber());
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
            List<Option> options = Option.parseCSV(optionInputStream);  // Utilisation correcte de InputStream
            System.out.println("Afficher les options :");
            for (Option option : options) {
                System.out.println("Option Name = " + option.getName());
                System.out.println("Option Code = " + option.getCode());
                System.out.println("Option Price = " + option.getPrice());
                optionRepository.save(option);
            }
            System.out.println("\n");

            // Traiter le fichier des espaces de travail
            InputStream workspaceInputStream = workspaceFile.getInputStream();
            List<Workspace> workspaces = Workspace.parseCSV(workspaceInputStream);  // Utilisation correcte de InputStream

            System.out.println("Afficher les espaces de travail :");
            for (Workspace workspace : workspaces) {
                System.out.println("Workspace Name = " + workspace.getName());
                System.out.println("Workspace Price = " + workspace.getPrice());
                workspaceRepository.save(workspace);
            }
            System.out.println("\n");


            // Traiter le fichier des réservations (si vous l'implémentez)
            InputStream reservationInputStream = reservationFile.getInputStream();
            List<Reservation> reservations = Reservation.parseCSV(reservationInputStream, workspaceRepository, clientRepository, optionRepository);
            System.out.println("Aprops des reservation");
            for (int i = 0; i < reservations.size(); i++) {
                System.out.println("Date :" + reservations.get(i).getDate());
                System.out.println("Reference :" + reservations.get(i).getRef());
                System.out.println("Heuredebut :" + reservations.get(i).getStartHour());
                System.out.println("HeureFin :" + reservations.get(i).getEndHour());
                System.out.println("Durée :" + reservations.get(i).getDuration());
                System.out.println("Status :" + reservations.get(i).getStatus());
                System.out.println("Numero du client :" + reservations.get(i).getClient().getId());
                System.out.println("Nom du workspace :" + reservations.get(i).getWorkspace().getName());
                System.out.println("Debut des options :");

                Reservation res  = reservationRepository.save(reservations.get(i));

                for (int j = 0; j < res.getOptions().size(); j++) {
                    res.getOptions().get(j).setReservation(res);
                    ReservationOption resop = res.getOptions().get(j);
                    reservationOptionRepository.save(resop) ;
                }

            }

            /*Import payment file */
            InputStream paymentInputStream = reservationFile.getInputStream();
            List<Payment> liste_payment  = Payment.importCsv(paymentInputStream);
                paymentRepository.saveAll(liste_payment);


            System.out.println("ImporterCSV effectuer");

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'importation des fichiers";
        }

        return "Importation réussie !";  // Ou la vue que vous souhaitez afficher après l'importation
    }
}
