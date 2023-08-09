package com.CTC.service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CTC.entity.Booking;
import com.CTC.entity.Payment;
import com.CTC.repository.repository.PaymentRepository;
import com.CTC.service.AuthService;


@Service
public class PaymentServiceImplementation implements PaymentService{
	@Autowired AuthService authService;
@Autowired PaymentRepository repo;
	public Payment createPayment(Booking booking) {

	        Payment payment = new Payment();
	        payment.setBooking(booking);
	 payment.setUtenteId(booking.getUser().getId());
	
	        payment.setAmount(booking.getTotalToPay()); // Set the payment amount from the booking's totalToPay
	        payment.setPaymentDateTime(LocalDateTime.now()); // Set the payment date and time as the current time
	        payment.setPaid(false); // By default, set the payment as unpaid
	        // Add any other necessary payment properties such as creditCard, secretCode, alfaCode, etc.
	        BigDecimal amountInCents = booking.getTotalToPay().multiply(BigDecimal.valueOf(1000));
	        payment.setAmountInCents(amountInCents.longValue()); // Convert the calculated BigDecimal to a long

	        return payment;
	    }

	public Payment createPayment(Payment payment) {
		return payment;
	}
	public List<Payment> getAllPayments(){
		List<Payment> list= repo.findAll();
		return list;
	}
	public List<Payment> getUserPayments(Long id){
		
		List<Payment> list=repo.findByUtenteId(id);
		
		return list;
		
	}
}
