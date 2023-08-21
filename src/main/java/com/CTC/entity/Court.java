package com.CTC.entity;

import com.CTC.enums.CourtType;
import com.CTC.enums.TypeField;
import jakarta.persistence.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "courts")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeField typeField;

    @Column(nullable = false)
    private Long capacity;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double priceSocio;
    
    @Enumerated(EnumType.STRING)
    private CourtType courtType;
    
    private Boolean isActive;
}
