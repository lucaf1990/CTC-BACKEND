package com.CTC.service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CTC.entity.Booking;
import com.CTC.entity.ERole;
import com.CTC.entity.Image;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.exception.MyAPIException;
import com.CTC.exception.ResourceNotFoundException;
import com.CTC.payload.LoginDto;
import com.CTC.payload.RegisterDto;
import com.CTC.repository.RoleRepository;
import com.CTC.repository.UserRepository;
import com.CTC.repository.repository.BookingRepository;
import com.CTC.repository.repository.ImageRepository;
import com.CTC.repository.repository.PaymentRepository;
import com.CTC.security.JwtTokenProvider;
import com.CTC.service.service.ImageService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter


@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
 
	@Autowired
	ImageService imageService;
	@Autowired
	PaymentRepository payRepo;
	@Autowired
	BookingRepository bookingRepo;
	@Autowired
	EmailService emailService;
private ImageRepository imageRepo;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        
    	Authentication authentication = authenticationManager.authenticate(
        		new UsernamePasswordAuthenticationToken(
        				loginDto.getUserName(), loginDto.getPassword()
        		)
        ); 
    	System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        System.out.println(token);
        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        // add check for username exists in database
        if(userRepository.existsByUserName(registerDto.getUserName())){
            throw new MyAPIException(HttpStatus.BAD_REQUEST, "Username già presente nel database! Inserisci un altro Username");
        }

        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new MyAPIException(HttpStatus.BAD_REQUEST, "Email già presente nel database! Contattaci se hai necessità di recuperare la password");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUserName(registerDto.getUserName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setDateOfBirth(registerDto.getDateOfBirth());
        user.setActive(true);
        user.setLastName(registerDto.getLastName());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setIsMember(false);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);

        String confirmationToken = UUID.randomUUID().toString(); // Generate confirmation token
        user.setConfirmationToken(confirmationToken); // Set confirmation token

        userRepository.save(user); // Save user with confirmation token

        String confirmationLink = "http://localhost:3000/email-confirmation/" + confirmationToken;
        String expectedLink= confirmationLink;
        emailService.sendConfirmationEmail(user.getEmail(), confirmationLink, user.getName(), expectedLink);
        return "Registrazione avvenuta con successo";
    }
    
    public ERole getRole(String role) {
    	if(role.equals("ADMIN")) return ERole.ROLE_ADMIN;
    	else if(role.equals("MODERATOR")) return ERole.ROLE_MODERATOR;
    	else return ERole.ROLE_USER;
    }
    public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			User user = userRepository.findByUserName(username);
			return user;
		} else {
			// user non autenicato
			return null;
		}
	}
	
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public void changePermissions(String s,ERole roles) {
		Set<Role> role = new HashSet<Role>();
		role.add(roleRepository.findByRoleName(roles));
		User u = userRepository.findByEmail(s);
		u.setRoles(role);
	
		userRepository.save(u);
	}

	public User updateUserDataAndPermissions(Long id, User updatedUser) {
        // Assume you have a way to identify the user, like using the user ID
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", null, 0));

        // Update user data
        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setLastName(updatedUser.getLastName());
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setUserName(updatedUser.getUserName());
        userToUpdate.setActive(updatedUser.getActive());
        userToUpdate.setIsMember(updatedUser.getIsMember());
        userToUpdate.setScadenzaCertificato(updatedUser.getScadenzaCertificato());
      
        userToUpdate.setRoles(updatedUser.getRoles());

        return userRepository.save(userToUpdate);
    }

	public String removeUtente(Long userId) {
	    // Retrieve the user by ID
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
	    for (Payment payment : user.getPayments()) {
            payRepo.delete(payment);
        }
	    // Delete bookings associated with the user
	    for (Booking booking : user.getBookings()) {
	        // Delete payments associated with the booking
	       

	        bookingRepo.delete(booking);
	    }

	    // Delete the user
	    userRepository.deleteById(userId);

	    return "User deleted along with associated payments and bookings";
	}

	public User findById(Long id) {
		return userRepository.findById(id).get();
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUserName(username);
	}

	public List<User> findAllUtente() {
		return userRepository.findAll();
	}
	public User updateUtenteImage(Long iduser, MultipartFile file) {
		if(userRepository.existsById(iduser)) {
		Image i = (Image) imageService.saveImage(file);
		User u = userRepository.findById(iduser).get();
		u.setUrlImmagineProfilo(i.getUrl());
		
		return userRepository.save(u);}
		else {
			throw new MyAPIException(HttpStatus.NOT_FOUND, "utente non trovato");
		}
	}
	
	public boolean isModerator(Long id) {
	    User user = userRepository.findById(id).orElse(null); 
	    if (user != null && user.getRoles() != null) {
	        for (Role role : user.getRoles()) {
	            if ("ROLE_MODERATOR".equals(role.getRoleName())) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
    @Override
    public String generateResetToken(User user) {
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user); // Save the updated user with reset token
        
        return resetToken;
    }
    @Override
    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
}
