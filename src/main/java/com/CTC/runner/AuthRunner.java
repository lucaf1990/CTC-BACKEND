package com.CTC.runner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.CTC.entity.Booking;
import com.CTC.entity.ERole;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.payload.ChangePermissionsDto;
import com.CTC.repository.RoleRepository;
import com.CTC.repository.UserRepository;
import com.CTC.service.AuthService;
import com.CTC.service.AuthServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder


@Component
public class AuthRunner implements ApplicationRunner {
	
	@Autowired RoleRepository roleRepository;
	@Autowired UserRepository userRepository;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired AuthServiceImpl authService;
	
	private Set<Role> adminRole;
	private Set<Role> moderatorRole;
	private Set<Role> userRole;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("CTC CARPENEDOLO - BACKEND LIVE SERVER - CONNECTION CREATED - DATABASE CREATED/UPDATED CORRECTLY");
		// Metodo da lanciare solo la prima volta
		// Serve per salvare i ruoli nel DB
		//setRoleDefault();
		
		//ChangePermissionsDto cpd = new ChangePermissionsDto();
		//cpd.setEmail("2@example.com");
		//cpd.setRoles(ERole.ROLE_MODERATOR);

		//authService.changePermissions(cpd.getEmail(), cpd.getRoles());
	}
	
	private void setRoleDefault() {
		Role admin = new Role();
		admin.setRoleName(ERole.ROLE_ADMIN);
		roleRepository.save(admin);
		
		Role user = new Role();
		user.setRoleName(ERole.ROLE_USER);
		roleRepository.save(user);
		
		Role moderator = new Role();
		moderator.setRoleName(ERole.ROLE_MODERATOR);
		roleRepository.save(moderator);
		
//		adminRole = new HashSet<Role>();
//		adminRole.add(admin);
//		adminRole.add(moderator);
//		adminRole.add(user);
//		
//		moderatorRole = new HashSet<Role>();
//		moderatorRole.add(moderator);
//		moderatorRole.add(user);
//		
//		userRole = new HashSet<Role>();
//		userRole.add(user);
	}
	
	

}
