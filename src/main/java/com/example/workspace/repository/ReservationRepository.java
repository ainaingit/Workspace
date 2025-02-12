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
            "AND r.workspace.id = :workspaceId " +
            "AND (r.startHour < :endTime AND r.endHour > :startTime)")
    List<Reservation> findConflictingReservations(@Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime,
                                                  @Param("workspaceId") Long workspaceId);

    List<Reservation> findByDateAndWorkspaceId(LocalDate date, Long workspaceId);

    @Query("SELECT sum(r.duration ) FROM Reservation r WHERE r.date = :date " +
            "AND r.workspace.id = :workspaceId ")
    int SumdurationByDateAndWorkspaceId(LocalDate date, Long workspaceId);
    List<Reservation> findByDate(LocalDate date);

    Reservation findTopByOrderByRefDesc();

    Reservation findByRef(String ref);
}
