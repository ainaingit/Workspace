package com.example.workspace.repository;

import com.example.workspace.vue.DiviseHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiviseHoursRepository extends JpaRepository<DiviseHours, String> {
    // Ici, on peut ajouter des méthodes spécifiques si besoin
    @Query("SELECT d FROM DiviseHours d ORDER BY d.reservations_count DESC")
    List<DiviseHours> findAllTopHours();

}
