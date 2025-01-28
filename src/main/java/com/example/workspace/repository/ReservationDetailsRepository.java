package com.example.workspace.repository;

import com.example.workspace.objet.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDetailsRepository  extends JpaRepository<ReservationDetails, Long> {
    // Méthode pour récupérer les ReservationDetails par idclient
    List<ReservationDetails> findByClientId(Long clientId);
}