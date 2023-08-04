	package com.CTC.service;
	
	import com.CTC.payload.LoginDto;
	import com.CTC.payload.RegisterDto;
	
public interface AuthService {
    
	String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
    
}
