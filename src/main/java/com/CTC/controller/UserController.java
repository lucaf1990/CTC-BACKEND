package com.CTC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.CTC.entity.User;

import com.CTC.service.AuthServiceImpl;



@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 60000000)  
public class UserController {
	@Autowired
	AuthServiceImpl uSer;

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> trovaUserbyId(@PathVariable Long id) {
		return new ResponseEntity<User>(uSer.findById(id), HttpStatus.OK);

	}
	
	@GetMapping("/username/{userName}")
	
	public ResponseEntity<User> trovaUsername(@PathVariable String userName) {
		return new ResponseEntity<User>(uSer.findByUsername(userName), HttpStatus.OK);
		
	}
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> eliminaByID(@PathVariable Long id) {
		return new ResponseEntity<String>(uSer.removeUtente(id), HttpStatus.OK);

	}
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody User u){
		return new ResponseEntity<User>(uSer.updateUtente(u),HttpStatus.OK);
	}
	
	

}