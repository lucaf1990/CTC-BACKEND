package com.CTC.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CTC.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Payment findByBookingId(Long bookingId);
}
