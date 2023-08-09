package com.CTC.repository.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CTC.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Payment findByBookingId(Long bookingId);

	List<Payment> findByUtenteId(Long id);
}
