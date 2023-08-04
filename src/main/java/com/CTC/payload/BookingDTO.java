package com.CTC.payload;

import java.time.LocalDateTime;

import com.CTC.entity.Court;
import com.CTC.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingDTO {

	private Long courtId;
    private LocalDateTime bookingDateTime;
    private Long userId;
    private int hours;
}
