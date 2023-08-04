package com.CTC.service.service;

import com.CTC.entity.Booking;
import com.CTC.payload.BookingDTO;

public interface BookingService {
	public Booking createBooking(BookingDTO bookingRequest);
	public Booking updateBooking(Booking booking);
	public void deleteBooking(Long bookingId);
}
