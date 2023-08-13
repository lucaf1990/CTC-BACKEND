package com.CTC.service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CTC.entity.Booking;
import com.CTC.entity.Court;
import com.CTC.entity.Payment;
import com.CTC.entity.User;
import com.CTC.enums.CourtType;
import com.CTC.enums.TypeField;
import com.CTC.payload.BookingDTO;
import com.CTC.payload.BookingDTO2;
import com.CTC.repository.UserRepository;
import com.CTC.repository.repository.BookingRepository;
import com.CTC.repository.repository.CourtRepository;
import com.CTC.repository.repository.PaymentRepository;
import com.CTC.service.AuthService;
import com.CTC.service.AuthServiceImpl;



@Service
public class BookingServiceImplementation implements BookingService{

	
	 @Autowired
	    private BookingRepository bookingRepository;
	 @Autowired UserRepository userRepository;
	    @Autowired
	    private CourtRepository courtRepository;
	    @Autowired AuthServiceImpl authService;
	    @Autowired 
	    private PaymentServiceImplementation paymentService;
	    @Autowired 
	    private PaymentRepository paymentRepository;
	    private BigDecimal totalToPay(double price, int hours) {
	        double total = price * hours;
	        // Convert the total to a BigDecimal with two decimal places
	        return BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
	    }
	    public Booking createBooking(BookingDTO bookingRequest) {
	        // Retrieve the court from the database using the courtId from the DTO
	    	
	        Court court = courtRepository.findById(bookingRequest.getCourtId())
	                .orElseThrow(() -> new IllegalArgumentException("Court not found with ID: " + bookingRequest.getCourtId()));
	        
	        User user = userRepository.findById(bookingRequest.getUserId())
	                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + authService.getCurrentUser().getId()));
	        boolean isModerator = authService.isModerator(user.getId());
	        BigDecimal totalToPay;
	        if (isModerator) {
	            totalToPay = totalToPay(court.getPriceSocio(), bookingRequest.getHours());
	        } else {
	            totalToPay = totalToPay(court.getPrice(), bookingRequest.getHours());
	        }

	        LocalDateTime bookingDateTime = bookingRequest.getBookingDateTime();
	        if (bookingDateTime.isBefore(LocalDateTime.now())) {
	            throw new IllegalArgumentException("Booking date and time cannot be in the past.");
	        }

	        // Create a new booking
	        if(user.getActive()) { Booking newBooking = new Booking();
	        newBooking.setTotalToPay(totalToPay);
	        newBooking.setCourt(court);
	        newBooking.setBookingDateTime(bookingDateTime);
	        newBooking.setBookingEnds(bookingDateTime.plusHours(bookingRequest.getHours()));
	        newBooking.setConfirmed(true); // You can set the default value for confirmed as needed
	        newBooking.setUser(user);//per vedere  se è memebro o mo 
	    
	        newBooking.setHours(bookingRequest.getHours());

	        // Check for booking conflicts before saving the booking
	        if (isBookingConflict(newBooking)) {
	            throw new IllegalArgumentException("Booking conflict detected");
	        }

	        // Save the booking to the database
	        Booking savedBooking = bookingRepository.save(newBooking);

	        // Create and save the payment object
	        Payment payment = paymentService.createPayment(savedBooking);
	        paymentRepository.save(payment); // Assuming you have a paymentRepository to save the Payment object

	        return savedBooking;} else {
	        	throw new IllegalStateException("UTENTE NON ATTIVO");
	        }
	       
	    }
	    public Booking createBookingAdmin(BookingDTO2 bookingRequest) {
	        // Retrieve the court from the database using the courtId from the DTO
	        Court court = courtRepository.findById(bookingRequest.getCourtId())
	                .orElseThrow(() -> new IllegalArgumentException("Court not found with ID: " + bookingRequest.getCourtId()));
	        
	        User user = userRepository.findById(bookingRequest.getUserId())
	                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + authService.getCurrentUser().getId()));
	       
	        BigDecimal totalToPay;
	        if (bookingRequest.getSocio()) {
	            totalToPay = totalToPay(court.getPriceSocio(), bookingRequest.getHours());
	        } else {
	            totalToPay = totalToPay(court.getPrice(), bookingRequest.getHours());
	        }

	        LocalDateTime bookingDateTime = bookingRequest.getBookingDateTime();
	        if (bookingDateTime.isBefore(LocalDateTime.now())) {
	            throw new IllegalArgumentException("Booking date and time cannot be in the past.");
	        }
	
	       


	        // Create a new booking
	        Booking newBooking = new Booking();
	        newBooking.setTotalToPay(totalToPay);
	        newBooking.setCourt(court);
	        newBooking.setBookingDateTime(bookingDateTime);
	        newBooking.setBookingEnds(bookingDateTime.plusHours(bookingRequest.getHours()));
	        newBooking.setConfirmed(true); // You can set the default value for confirmed as needed
	       
	    newBooking.setUser(user);
	        newBooking.setHours(bookingRequest.getHours());
	        newBooking.setNotePrenotazione(bookingRequest.getNotePrenotazione());

	        // Check for booking conflicts before saving the booking
	        if (isBookingConflict(newBooking)) {
	            throw new IllegalArgumentException("Booking conflict detected");
	        }

	        // Save the booking to the database
	        Booking savedBooking = bookingRepository.save(newBooking);

	        // Create and save the payment object
	        Payment payment = paymentService.createPayment(savedBooking);
	        paymentRepository.save(payment); // Assuming you have a paymentRepository to save the Payment object

	        return savedBooking;
	    }


	    public Booking updateBooking(Booking booking) {
	        // Check if the booking exists in the database
	        Booking existingBooking = bookingRepository.findById(booking.getId())
	                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + booking.getId()));

	        // Check if the booking start time is at least 24 hours from now
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        LocalDateTime bookingStartDateTime = booking.getBookingDateTime();
	        long hoursDifference = ChronoUnit.HOURS.between(currentDateTime, bookingStartDateTime);

	        if (hoursDifference <= 24) {
	            throw new IllegalArgumentException("The booking cannot be updated as it starts within the next 24 hours. Please select a different time.");
	        }

	        // Update the booking details
	        existingBooking.setBookingDateTime(booking.getBookingDateTime());
	        existingBooking.setConfirmed(booking.isConfirmed());
existingBooking.setBookingEnds(booking.getBookingDateTime().plusHours(booking.getHours()));
existingBooking.getUser();

	        // Check for booking conflicts before saving the updated booking
	        if (isBookingConflict(existingBooking)) {
	            throw new IllegalArgumentException("Booking conflict detected. Please select a different time.");
	        }

	        // Save the updated booking to the database
	        existingBooking = bookingRepository.save(existingBooking);

	        // Update the associated payment
	        Payment payment = paymentRepository.findByBookingId(existingBooking.getId());
	        if (payment != null) {
	            // Update payment details, if necessary
	            // For example: payment.setAmount(newPaymentAmount);
	            paymentRepository.save(payment);
	        }

	        return existingBooking;
	    }


	    public void deleteBooking(Long bookingId) {
	        // Check if the booking exists in the database
	        Booking existingBooking = bookingRepository.findById(bookingId)
	                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

	        // Check if the booking start time is at least 24 hours from now
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        LocalDateTime bookingStartDateTime = existingBooking.getBookingDateTime();
	        long hoursDifference = ChronoUnit.HOURS.between(currentDateTime, bookingStartDateTime);

	        if (hoursDifference <= 24) {
	            throw new IllegalArgumentException("LE PRENOTAZIONI POSSO ESSERE ELIMINATE ENTRO 24 PRIMA DELL'INIZIO");
	        }

	        // Delete the associated payment, if it exists
	        Payment payment = paymentRepository.findByBookingId(bookingId);
	        if (payment != null) {
	            paymentRepository.delete(payment);
	        }

	        // Delete the booking from the database
	        bookingRepository.delete(existingBooking);
	 
	    }
	    public void deleteBookingAdmin(Long bookingId) {
	        // Check if the booking exists in the database
	        Booking existingBooking = bookingRepository.findById(bookingId)
	                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

	    
	       
	        
	        Payment payment = paymentRepository.findByBookingId(bookingId);
	        if (payment != null) {
	            paymentRepository.delete(payment);
	        }

	        // Delete the booking from the database
	        bookingRepository.delete(existingBooking);
	 
	    }

	    public Booking getBookingById(Long bookingId) {
	        return bookingRepository.findById(bookingId)
	                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
	   
	    
	    
	    
	    
	    
	    }

	    
	    //refund
	    // Other methods for retrieving bookings based on different criteria can also be implemented here
//ci dara i tasti rosso 
	    private boolean isBookingConflict(Booking newBooking) {
	        LocalDateTime bookingStart = newBooking.getBookingDateTime();
	        LocalDateTime bookingEnd = newBooking.getBookingDateTime().plusHours(1);

	        List<Booking> existingBookings = bookingRepository.findOverlappingBookings(
	                newBooking.getCourt(), bookingEnd, bookingStart
	        );

	        // Check if there is any existing booking for the same court and overlapping date/time
	        return !existingBookings.isEmpty();
	    }
	
	    public List<Booking> getAllBookings(Long userId) {
	        Optional<User> user = userRepository.findById(userId);
	        
	        if (user.isPresent()) {
	            return bookingRepository.findAllByUser_Id(userId);
	        } else {
	            throw new Error("User not found with ID: " + userId);
	        }
	    }

	    public List<Booking> getAllUserBookings() {
	        
	       List<Booking> myList= bookingRepository.findAll();
	            return myList;
	        
	    }
	    public List<Booking> getAllTennisBookings() {
	        List<Booking> allBookings = bookingRepository.findAll(); // Assuming bookingRepository can retrieve all bookings from the database

	        return allBookings.stream()
	                .filter(booking -> {
	                    Court court = booking.getCourt();
	                    return court != null && court.getTypeField() == TypeField.TENNIS;
	                })
	                .collect(Collectors.toList());
	    }
	    public List<Booking> getAllCalcettoBookings() {
	        List<Booking> allBookings = bookingRepository.findAll(); // Assuming bookingRepository can retrieve all bookings from the database

	        return allBookings.stream()
	                .filter(booking -> {
	                    Court court = booking.getCourt();
	                    return court != null && court.getTypeField() == TypeField.CALCETTO;
	                })
	                .collect(Collectors.toList());
	    }
	    public List<Booking> getAllBeachVolleyBookings() {
	        List<Booking> allBookings = bookingRepository.findAll(); // Assuming bookingRepository can retrieve all bookings from the database

	        return allBookings.stream()
	                .filter(booking -> {
	                    Court court = booking.getCourt();
	                    return court != null && court.getTypeField() == TypeField.BEACHVOLLEY;
	                })
	                .collect(Collectors.toList());
	    }
}
