package com.example.workspace.repository;

import com.example.workspace.vue.ChiffreAffaireTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiffreAffaireTotalRepository extends JpaRepository<ChiffreAffaireTotal, Long> {
}