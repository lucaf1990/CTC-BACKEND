package com.CTC.payload;

import com.CTC.entity.ERole;
import com.CTC.entity.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePermissionsDto {
    private String email;
  
    private ERole roles;

   
}