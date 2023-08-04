package com.CTC.payload;

import java.util.HashSet;
import java.util.Set;

import com.CTC.entity.ERole;
import com.CTC.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
	private String username;
    private String accessToken;
    private String tokenType = "Bearer";
    private Set<ERole> roles= new HashSet<>();
}
