package com.example.workspace.entity;

import jakarta.persistence.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Nom de l'espace de travail
    private int price ;
    // Relation avec les réservations
    @OneToMany(mappedBy = "workspace")
    private List<Reservation> reservations;

    public Workspace() {}

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    public static List<Workspace> parseCSV(InputStream inputStream) {
        List<Workspace> workspaces = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT
                             .withFirstRecordAsHeader() // Ignore la première ligne comme en-tête
                             .withIgnoreHeaderCase()    // Ignorer la casse des noms de colonnes
                             .withTrim()                // Supprimer les espaces inutiles
             )) {

            // Parcourir les lignes suivantes
            for (CSVRecord csvRecord : csvParser) {
                Workspace workspace = new Workspace() ;
                workspace.setName(csvRecord.get("nom"));              // Utiliser les noms d'en-têtes
                workspace.setPrice(Integer.parseInt(csvRecord.get("prix_heure")));

                workspaces.add(workspace);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }

        return workspaces;
    }
}
