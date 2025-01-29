package com.example.workspace.entity;

import jakarta.persistence.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code ;
    private String name;  // Nom de l'option (par exemple, imprimante, vidéoprojecteur, etc.)
    private double price; // Prix de l'option

    public Option() {}

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static List<Option> parseCSV(InputStream inputStream) {
        List<Option> options = new ArrayList<>();

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
                Option option = new Option();
                option.setCode(csvRecord.get("CODE"));
                option.setName(csvRecord.get("OPTION"));
                option.setPrice(Double.parseDouble(csvRecord.get("PRIX")));

                options.add(option);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }

        return options;
    }
}
