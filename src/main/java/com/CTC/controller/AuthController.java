package com.CTC.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CTC.entity.ConfirmationTokenRequest;
import com.CTC.entity.ERole;
import com.CTC.entity.User;
import com.CTC.payload.JWTAuthResponse;
import com.CTC.payload.LoginDto;
import com.CTC.payload.RegisterDto;
import com.CTC.repository.UserRepository;
import com.CTC.service.AuthService;
import com.CTC.service.AuthServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;





@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 60000000)
public class AuthController {
	@Autowired
    private AuthService authService;
    @Autowired
	UserRepository userRepository;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PersistenceContext
    private EntityManager entityManager;
    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        User user = userRepository.findByUserName(loginDto.getUserName());

        if (user == null) {
            // Return error response indicating user not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        if (!user.getEmailConfirmed()) {
            // Return error response indicating unconfirmed email
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not confirmed.");
        }

        String token = authService.login(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setUsername(loginDto.getUserName());
        jwtAuthResponse.setAccessToken(token);

        if (user.getRoles().isEmpty()) {			
            jwtAuthResponse.getRoles().add(ERole.ROLE_USER);
        } else {
            Set<ERole> roles = new HashSet<>();
            user.getRoles().forEach(role -> roles.add(role.getRoleName()));
            jwtAuthResponse.setRoles(roles);
        }

        return ResponseEntity.ok(jwtAuthResponse);
    }


    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/confirm-email/{token}")
    public ResponseEntity<String> confirmEmail(@PathVariable String token) {
        // Retrieve user by token
        User user = userRepository.findByConfirmationToken(token);
           
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        user.setEmailConfirmed(true);
        user.setConfirmationToken(null);

        User updatedUser = userRepository.save(user); // Save the updated user

        if (updatedUser != null) {
            return ResponseEntity.ok("Email confirmed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user.");
        }
    }
}
