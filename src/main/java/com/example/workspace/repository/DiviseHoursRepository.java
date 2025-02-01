package com.example.workspace.repository;

import com.example.workspace.vue.DiviseHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiviseHoursRepository extends JpaRepository<DiviseHours, String> {
    // Ici, on peut ajouter des méthodes spécifiques si besoin
}
