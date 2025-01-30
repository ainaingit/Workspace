package com.example.workspace.repository;

import com.example.workspace.vue.ChiffreAffaireParJour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChiffreAffaireRepository extends JpaRepository<ChiffreAffaireParJour, LocalDate> {

    // Filtrer par date (si nécessaire)
    List<ChiffreAffaireParJour> findByDatePaiementBetween(LocalDate start, LocalDate end);
}