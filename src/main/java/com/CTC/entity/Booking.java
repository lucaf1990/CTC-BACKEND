package com.CTC.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = "bookings")
    private User user;
 private Boolean isPaid;
    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court; 
    @Column(nullable = false)
    private int hours;
    
    @Column(nullable = false)
    private LocalDateTime bookingDateTime; 
  
    private LocalDateTime bookingEnds; 
    
    @Column(nullable = false)
    private boolean confirmed; 

    private BigDecimal totalToPay;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = {"bookingReview"})
	private Set<Reviews> reviews = new HashSet<Reviews>();
    @Column(length = 2000)
    private String notePrenotazione;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "booking")
    @JsonIgnoreProperties(value = {"booking"})
    private Set<Payment> payments = new HashSet<>();
  
}