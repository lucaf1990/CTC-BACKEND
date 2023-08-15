	package com.CTC.service;
	
	import java.util.Optional;

import com.CTC.entity.User;
import com.CTC.payload.LoginDto;
	import com.CTC.payload.RegisterDto;
	
public interface AuthService {
    
	String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
    String generateResetToken(User user);
    void updateUserPassword(User user, String newPassword);
}
