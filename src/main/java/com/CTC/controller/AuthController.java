package com.CTC.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CTC.entity.Booking;
import com.CTC.entity.ConfirmationTokenRequest;
import com.CTC.entity.ERole;
import com.CTC.entity.PasswordResetRequest;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.payload.JWTAuthResponse;
import com.CTC.payload.LoginDto;
import com.CTC.payload.RegisterDto;
import com.CTC.repository.UserRepository;
import com.CTC.service.AuthService;
import com.CTC.service.AuthServiceImpl;
import com.CTC.service.EmailService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter




@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 60000000)
public class AuthController {
	@Autowired
    private AuthService authService;
    @Autowired
	UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PersistenceContext
    private EntityManager entityManager;
    
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        User user = userRepository.findByUserName(loginDto.getUserName());

        if (user == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        if (!user.getEmailConfirmed()) {
         
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

        User updatedUser = userRepository.save(user); 

        if (updatedUser != null) {
            return ResponseEntity.ok("UNA EMAIL PER IL RECUPERO PASSWORD E' STATA INVIATA CON SUCCESSO ");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user.");
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest resetRequest) {
    	  User user = userRepository.findByEmail(resetRequest.getEmail());

    	    if (user == null) {
    	        return ResponseEntity.badRequest().body("NESSUN UTENTE REGISTRATO CON QUESTO INDIRIZZO EMAIL");
    	    }

    	    String resetToken = authService.generateResetToken(user);
    	    String resetLink = "http://localhost:3000/reset-password/" + resetToken;
    	  
    	    try {
    	        String emailSubject = "CTC - Recupera password";
    	        String emailBody = "Clicca sul seguente link se vuoi modificare la tua password " + resetLink;
    	        
    	        SimpleMailMessage mailMessage = new SimpleMailMessage();
    	        mailMessage.setTo(user.getEmail());
    	        mailMessage.setSubject(emailSubject);
    	        mailMessage.setText(emailBody);
    	        
    	        mailSender.send(mailMessage);
    	        
    	        return ResponseEntity.ok("UNA EMAIL PER IL RECUPERO PASSWORD E' STATA INVIATA CON SUCCESSO ");
    	    } catch (MailException e) {
    	        // Handle email sending failure
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
    	    }
    }
    @PutMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(
        @PathVariable String token,
        @RequestBody PasswordResetRequest resetRequest
    ) {
        User user = userRepository.findByResetToken(token);

        if (user == null) {
            return ResponseEntity.badRequest().body("TOKEN PER IL RECUPERO PASSWORD INVALIDO O SCADUTO");
        }


        authService.updateUserPassword(user, resetRequest.getNewPassword());


        user.setResetToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("LA PASSWORD E' STATA AGGIORNATA IN MODO CORRETTO");
    }

}
