package com.example.workspace.entity;

import com.example.workspace.repository.ClientRepository;
import com.example.workspace.repository.OptionRepository;
import com.example.workspace.repository.WorkspaceRepository;
import jakarta.persistence.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long id;

    @Column(unique=true)
    private String number;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client() {
    }

    public Client(Long id, String number) {
        this.setId(id);
        this.setNumber(number);
    }

    public static List<Client> parseCSV(InputStream inputStream
                                             ) {  // Ajouter l'OptionRepository pour trouver les options
        List<Client> clients = new ArrayList<>();

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
                Client client  = new Client() ;
                // Utiliser les noms d'en-têtes pour peupler l'objet Reservation
                client.setNumber(csvRecord.get("client"));
                clients.add(client) ;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients ;
    }
}
