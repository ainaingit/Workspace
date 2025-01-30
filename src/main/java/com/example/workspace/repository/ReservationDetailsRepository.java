package com.example.workspace.repository;

import com.example.workspace.vue.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDetailsRepository  extends JpaRepository<ReservationDetails, Long> {
    // Méthode pour récupérer les ReservationDetails par idclient
    List<ReservationDetails> findByClientId(Long clientId);
}