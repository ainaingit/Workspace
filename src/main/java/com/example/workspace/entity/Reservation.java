package com.example.workspace.entity;

import com.example.workspace.repository.ClientRepository;
import com.example.workspace.repository.OptionRepository;
import com.example.workspace.repository.ReservationRepository;
import com.example.workspace.repository.WorkspaceRepository;
import jakarta.persistence.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;  // Date de la réservation (date sans heure)

    private String ref; // Référence générée automatiquement

    private LocalTime startHour;   // Heure de début de la réservation (en entier, ex: 8 pour 8h)
    private LocalTime endHour;     // Heure de fin de la réservation
    private int duration;          // Durée de la réservation en heures

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;  // Statut de la réservation (fait, payé, à payer, en attente)

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;  // L'espace de travail réservé

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;  // Le client qui a effectué la réservation

    @ManyToOne
    @JoinColumn(name = "reserveur_id")
    private Client reserveur;  // Le client qui a effectué la réservation

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationOption> options;

    public Reservation() {}


    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<ReservationOption> getOptions() {
        return options;
    }

    public void setOptions(List<ReservationOption> options) {
        this.options = options;
    }

    public Client getReserveur() {
        return reserveur;
    }

    public void setReserveur(Client reserveur) {
        this.reserveur = reserveur;
    }

    public Reservation(LocalDate date, LocalTime startHour, int duration, Workspace workspace, Client client,Client reserveur) {
        this.setDate(date);
        this.setStartHour(startHour);
        this.setEndHour(startHour.plusHours(duration));
        this.setDuration(duration);
        this.setStatus(ReservationStatus.valueOf("A_PAYER"));
        this.setWorkspace(workspace);
        this.setClient(client);
        this.setReserveur(reserveur);
    }

    public static List<Reservation> parseCSV(InputStream inputStream,
                                             WorkspaceRepository workspaceRepository,
                                             ClientRepository clientRepository,
                                             OptionRepository optionRepository) {
        List<Reservation> reservations = new ArrayList<>();
        // Définir le format de la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT
                             .withFirstRecordAsHeader()  // Ignore la première ligne comme en-tête
                             .withIgnoreHeaderCase()     // Ignorer la casse des noms de colonnes
                             .withTrim()                 // Supprimer les espaces inutiles
             )) {

            // Parcourir les lignes suivantes
            for (CSVRecord csvRecord : csvParser) {
                Reservation reservation = new Reservation();
                reservation.setRef(csvRecord.get("ref"));  // Utiliser la ref du CSV

                reservation.setWorkspace(workspaceRepository.findByName(csvRecord.get("espace")));
                reservation.setClient(clientRepository.findByNumber(csvRecord.get("client")));
                reservation.setDate(LocalDate.parse(csvRecord.get("date"), formatter));
                reservation.setStartHour(LocalTime.parse(csvRecord.get("heure_debut")));
                reservation.setDuration(Integer.parseInt(csvRecord.get("duree")));
                reservation.setEndHour(reservation.getStartHour().plusHours(Integer.parseInt(csvRecord.get("duree"))));
                reservation.setStatus(ReservationStatus.valueOf("A_PAYER"));

                // Traiter les options (ex: "opt1, opt3")
                String optionsColumn = csvRecord.get("option");
                List<ReservationOption> reservationOptions = new ArrayList<>();

                if (optionsColumn != null && !optionsColumn.isBlank()) {
                    for (String optionName : optionsColumn.split(",")) {
                        Option option = optionRepository.findByCode(optionName.trim().toUpperCase());
                        if (option != null) {
                            reservationOptions.add(new ReservationOption(reservation, option));
                        }
                    }
                }

                reservation.setOptions(reservationOptions);
                reservations.add(reservation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Trier la liste des réservations par ref
        reservations.sort(Comparator.comparing(Reservation::getRef));

        return reservations;
    }


}
