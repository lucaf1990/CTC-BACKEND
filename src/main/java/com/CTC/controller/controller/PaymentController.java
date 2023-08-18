package com.CTC.controller.controller;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CTC.entity.Booking;
import com.CTC.entity.Payment;
import com.CTC.entity.User;
import com.CTC.repository.repository.BookingRepository;
import com.CTC.repository.repository.PaymentRepository;
import com.CTC.service.EmailService;
import com.CTC.service.service.PaymentService;
import com.CTC.service.service.PaymentServiceImplementation;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/pay")
@CrossOrigin(origins = "*", maxAge = 6000000)
public class PaymentController {

    private static Gson gson = new Gson();

    @Autowired
    private PaymentServiceImplementation paymentService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EmailService emailSender;
    @Autowired PaymentRepository repo;
    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    static class CreatePayment {
        @SerializedName("items")
        Object[] items;

        public Object[] getItems() {
            return items;
        }
    }

    @GetMapping("/payments/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getAllPayment() {
        List<Payment> list = paymentService.getPayments();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/payment/{utenteId}")

    public ResponseEntity<List<Payment>> getPayments(@PathVariable Long utenteId) {
        List<Payment> list = paymentService.getUserPayments(utenteId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PutMapping("/markAsPaid/{id}")	
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> markAsPaid(@PathVariable Long id) {
    	
        return new ResponseEntity<Payment> (paymentService.markAsPaid(id), HttpStatus.OK);
    }
    
    static class CreatePaymentResponse {
        private String clientSecret;

        public CreatePaymentResponse(String clientSecret) {
            this.clientSecret = clientSecret;
	        }
	    }
	
	    @PostMapping("/create-payment")
	
	    public String createPayment(@RequestBody Payment payment) throws StripeException {
	        Stripe.apiKey = secretKey;
	
	        // Create a PaymentIntent with the calculated order amount and currency
	        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
	        		.setAmount(payment.getAmount().longValue() * 100L) // Convert euros to centsUse the amount in cents from the Payment entity
	                .setCurrency("eur")
	                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
	                .build();
	
	        PaymentIntent paymentIntent = PaymentIntent.create(params);
	
	        // Save the Payment entity in the database
	        paymentService.createPayment(payment);

	        // Return the client secret as a response
	        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(paymentIntent.getClientSecret());
	        return gson.toJson(paymentResponse);
	    }
	    
	    static int calculateOrderAmount(Object[] items) {
	        int totalAmount = 0;
	
	        // Assuming each item has a "price" field, you should adapt this based on your actual item structure
	        for (Object item : items) {
	            // Assuming you can access the price of each item through a getPrice() method,
	            // update this based on the actual structure of your items
	            int itemPrice = getItemPrice(item); // Replace getItemPrice with the actual method to get the price
	            totalAmount += itemPrice;
	        }
	
	        return totalAmount;
	    }
	    private static int getItemPrice(Object item) {
	        // Assuming the item has a "price" field, adapt this based on the actual structure of your items
	        // Replace "getPrice()" with the appropriate method to retrieve the price from each item
	        // For example, if each item is a separate class, you can return item.getPrice();
	        // If the items are Maps, you can return (int) item.get("price");
	        // Adapt this based on the actual structure of your items.
	        // For example, if the item is a separate class with a "getPrice()" method:
	        // return ((YourItemClass) item).getPrice();
	        
	        // Example assuming the item is a Map and the price is stored as an Integer:
	        return (int) ((Map<String, Object>) item).get("price");
	    }
	    @ExceptionHandler
	    public String handleError(StripeException e) {
	        return e.getMessage();
	    }
}