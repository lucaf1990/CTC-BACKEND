package com.CTC.service.service;

import com.CTC.entity.Booking;
import com.CTC.entity.Payment;


public interface PaymentService {

	public Payment createPayment(Booking booking);
	Payment createPayment(Payment payment);
}
