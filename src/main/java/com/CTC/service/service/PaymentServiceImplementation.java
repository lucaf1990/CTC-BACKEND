package com.CTC.service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CTC.entity.Booking;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.repository.repository.BookingRepository;
import com.CTC.repository.repository.PaymentRepository;
import com.CTC.service.AuthService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@Builder

@Service
public class PaymentServiceImplementation implements PaymentService{
	@Autowired AuthService authService;
@Autowired PaymentRepository repo;
@Autowired BookingRepository bookingRepo;
	public Payment createPayment(Booking booking) {

	        Payment payment = new Payment();
	        payment.setBooking(booking);
	 payment.setUtenteId(booking.getUser().getId());
	
	        payment.setAmount(booking.getTotalToPay()); 
	        payment.setPaymentDateTime(LocalDateTime.now()); 
	        payment.setPaid(false); 
	        BigDecimal amountInCents = booking.getTotalToPay().multiply(BigDecimal.valueOf(1000));
	        payment.setAmountInCents(amountInCents.longValue()); 

	        return payment;
	    }
	  public static void main() {
	        String secretKey = "";
	        Stripe.apiKey = secretKey;

	        String paymentIntentId = ""; 

	        try {
	            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
	            System.out.println("Payment Status: " + paymentIntent.getStatus());
	            System.out.println("Payment Amount: " + paymentIntent.getAmount());
	           
	        } catch (StripeException e) {
	            e.printStackTrace();
	        }
	    }
	public Payment createPayment(Payment payment) {
		return payment;
	}

	public List<Payment> getUserPayments(Long id){
		
		List<Payment> list=repo.findByUtenteId(id);
		
		return list;
		
	}
	public List<Payment> getPayments(){
		return repo.findAll();
	}
public Payment markAsPaid(Long id) {
	 Payment payment = repo.findById(id).orElse(null);

	    if (payment != null && !payment.isPaid()) {
	        payment.setPaid(true);
	        repo.save(payment);

	        
	        Booking booking = payment.getBooking();
	        if (booking != null) {
	            booking.setIsPaid(true);
	            bookingRepo.save(booking);
	        }

	        return payment;
	    } else {
	      
	        return null; 
	    }
}
}
