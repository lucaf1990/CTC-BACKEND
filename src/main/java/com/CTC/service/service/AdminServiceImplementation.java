package com.CTC.service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.CTC.entity.User;
import com.CTC.repository.UserRepository;
import com.CTC.service.AuthServiceImpl;

public class AdminServiceImplementation implements AdminService {
	
	@Autowired AuthServiceImpl userService;
	@Autowired UserRepository userRepo;

	public String isMemeber(User u, boolean memeber) {
		
		Optional<User> user= userRepo.findById(u.getId());
		if(user.isPresent()) {
			user.get();
		User user2= new User();
		user2.setIsMember(true);
		}
		else {
			return "Nessun utente trovato";
		}
		
return "Modifica avvenuta con successo";		
		
	}
	
}
