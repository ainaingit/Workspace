package com.example.workspace.repository;

import com.example.workspace.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.date = :date " +
            "AND (r.startHour BETWEEN :startTime AND :endTime " +
            "OR r.endHour BETWEEN :startTime AND :endTime " +
            "OR :startTime BETWEEN r.startHour AND r.startHour " +
            "OR :endTime BETWEEN r.startHour AND r.endHour)")
    List<Reservation> findConflictingReservations(@Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);

    List<Reservation> findByDate(LocalDate date);

    Reservation findTopByOrderByRefDesc();
}
