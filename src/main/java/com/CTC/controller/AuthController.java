package com.CTC.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CTC.entity.ERole;
import com.CTC.entity.User;
import com.CTC.payload.JWTAuthResponse;
import com.CTC.payload.LoginDto;
import com.CTC.payload.RegisterDto;
import com.CTC.repository.UserRepository;
import com.CTC.service.AuthService;
import com.CTC.service.AuthServiceImpl;





@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 60000000)
public class AuthController {
	@Autowired
    private AuthService authService;
    @Autowired
	UserRepository uSer;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
           	
    	String token = authService.login(loginDto);
		User u = uSer.findByUserName(loginDto.getUserName());

		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
		jwtAuthResponse.setUsername(loginDto.getUserName());
		jwtAuthResponse.setAccessToken(token);
	
		if(u.getRoles().isEmpty()) {			
		jwtAuthResponse.getRoles().add(ERole.ROLE_USER) ;		
		}else {			
			Set<ERole> a = new HashSet<>();
			u.getRoles().forEach(role-> a.add(role.getRoleName()));
			jwtAuthResponse.setRoles(a);
		}


		return ResponseEntity.ok(jwtAuthResponse);
    }

    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
