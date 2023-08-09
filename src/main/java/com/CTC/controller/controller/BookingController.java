package com.CTC.controller.controller;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.CTC.entity.Booking;
import com.CTC.payload.BookingDTO;
import com.CTC.service.service.BookingServiceImplementation;


@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*", maxAge = 60000000)  
public class BookingController {

    private final BookingServiceImplementation bookingService;

    @Autowired
    public BookingController(BookingServiceImplementation bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingRequest) {
        Booking createdBooking = bookingService.createBooking(bookingRequest);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping(value={"/{bookingId}"},consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long bookingId, @RequestBody Booking updatedBooking) {
        updatedBooking.setId(bookingId); // Set the ID from the path variable to the updated booking object
        Booking updated = bookingService.updateBooking(updatedBooking);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{bookingId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<Void>( HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<Booking> getSpecificBooking(@PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }
    @GetMapping("/all/tennis")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<List<Booking>> getAll (){
        List<Booking> booking = bookingService.getAllTennisBookings();
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }
    @GetMapping("/all/calcetto")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<List<Booking>> getAllCalcetto (){
        List<Booking> booking = bookingService.getAllCalcettoBookings();
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }
    @GetMapping("/all/beachVolley")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<List<Booking>> getAllBeach (){
        List<Booking> booking = bookingService.getAllBeachVolleyBookings();
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }
    @GetMapping(value={"/all/{userId}"},consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
    public ResponseEntity<List<Booking>> getAllBookings(@PathVariable Long userId){
        List<Booking> bookings = bookingService.getAllBookings(userId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    @GetMapping("/allUsersBookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings(){
        List<Booking> bookings = bookingService.getAllUserBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}

