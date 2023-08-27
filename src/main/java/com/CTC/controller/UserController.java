package com.CTC.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CTC.entity.ERole;
import com.CTC.entity.User;

import com.CTC.service.AuthServiceImpl;



@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 60000000)  
public class UserController {
	@Autowired
	AuthServiceImpl uSer;

	@GetMapping("/{id}")

	public ResponseEntity<User> trovaUserbyId(@PathVariable Long id) {
		return new ResponseEntity<User>(uSer.findById(id), HttpStatus.OK);

	}
	
	@GetMapping("/username/{userName}")

	public ResponseEntity<User> trovaUsername(@PathVariable String userName) {
		return new ResponseEntity<User>(uSer.findByUsername(userName), HttpStatus.OK);
		
	}
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllUser() {
		return new ResponseEntity<List<User>>(uSer.findAllUtente(), HttpStatus.OK);
		
	}
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> eliminaByID(@PathVariable Long id) {
		return new ResponseEntity<String>(uSer.removeUtente(id), HttpStatus.OK);

	}
	
	@PutMapping("/uploadimage/{id}")	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('MODERATOR')")
	public ResponseEntity<?> updateUtenteImage(@PathVariable Long id,@RequestParam("file") MultipartFile file){
		return new ResponseEntity<User>(uSer.updateUtenteImage(id, file),HttpStatus.OK);
	}
	
	 @PutMapping("/update/{id}")
	    public ResponseEntity<User> updateUser(
	            @RequestBody User updatedUser,
	            @PathVariable Long id
	
	        ) {
	        User updated = uSer.updateUserDataAndPermissions(id,updatedUser);
	        return ResponseEntity.ok(updated);
	    }
	 @GetMapping("/get")
	    public ResponseEntity<User> getSecret(
	    		@RequestBody User updatedUser,
	            @RequestParam Long id
	
	        ) {
	        User updated = uSer.findById(id);
	        return ResponseEntity.ok(updated);
	    }
}