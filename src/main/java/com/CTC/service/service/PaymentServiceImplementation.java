package com.CTC.service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CTC.entity.Booking;
import com.CTC.entity.Payment;
import com.CTC.repository.repository.PaymentRepository;
import com.CTC.service.AuthService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;


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
	  public static void main() {
	        String secretKey = "";
	        Stripe.apiKey = secretKey;

	        String paymentIntentId = ""; // Replace with the actual Payment Intent ID

	        try {
	            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
	            System.out.println("Payment Status: " + paymentIntent.getStatus());
	            System.out.println("Payment Amount: " + paymentIntent.getAmount());
	            // Retrieve and print other payment details
	        } catch (StripeException e) {
	            e.printStackTrace();
	        }
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
