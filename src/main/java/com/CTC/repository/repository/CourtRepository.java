package com.CTC.repository.repository;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CTC.entity.Court;

@Primary
public interface CourtRepository extends JpaRepository<Court, Long> {

	void save(Optional<Court> court2);

}
