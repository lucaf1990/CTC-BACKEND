package com.CTC.repository.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.CTC.entity.Booking;
import com.CTC.entity.Court;


public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	  @Query("SELECT b FROM Booking b WHERE b.court = ?1 AND b.bookingDateTime < ?2 AND b.bookingEnds > ?3")
	    List<Booking> findOverlappingBookings(Court court, LocalDateTime endDateTime, LocalDateTime startDateTime);
	  List<Booking> findAllByUser_Id(Long userId);
}
