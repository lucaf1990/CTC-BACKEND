package com.CTC.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingDTO2 {

	private Long courtId;
    private LocalDateTime bookingDateTime;
    private Long userId;
    private int hours;
    private String notePrenotazione;
    private Boolean socio;
   
}
