package com.example.workspace.repository;

import com.example.workspace.entity.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationOptionRepository  extends JpaRepository<ReservationOption, Long> {
}
