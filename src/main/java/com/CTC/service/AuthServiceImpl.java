package com.CTC.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.CTC.entity.ERole;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.exception.MyAPIException;
import com.CTC.payload.LoginDto;
import com.CTC.payload.RegisterDto;
import com.CTC.repository.RoleRepository;
import com.CTC.repository.UserRepository;
import com.CTC.security.JwtTokenProvider;


@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


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
        System.out.println(user);
        userRepository.save(user);

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
		User u = userRepository.findByEmail(s).get();
		u.setRoles(role);
	
		userRepository.save(u);
	}

	public User updateUtente(User e) {
		return userRepository.save(e);
	}


	public String removeUtente(Long id) {
		userRepository.deleteById(id);
		return "Evento eliminato";
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
	
}
