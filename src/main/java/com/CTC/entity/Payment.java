package com.CTC.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amountInCents;
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)

    private Booking booking; // Riferimento alla prenotazione associata al pagamento

    @Column(nullable = false)
    private BigDecimal amount; // Importo del pagamento
   
    @Column(nullable = false)
    private LocalDateTime paymentDateTime; // Data e ora del pagamento

    @Column(nullable = false)
    private boolean isPaid; // Indica se il pagamento è stato effettuato con successo
  
    private Long utenteId;


}