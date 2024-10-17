package com.jotech.usermanagmentsystemjotech.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jotech.usermanagmentsystemjotech.dto.UserDTO;
import com.jotech.usermanagmentsystemjotech.entity.AuthenticationResponse;
import com.jotech.usermanagmentsystemjotech.entity.User;
import com.jotech.usermanagmentsystemjotech.service.UserService;
import com.jotech.usermanagmentsystemjotech.util.JwtUtil;

@RestController
@RequestMapping("/user")
public class AuthenticationController {

	@Autowired
	private  AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

	@Autowired
	private  JwtUtil jwtTokenUtil;
	
	


	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) //AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					user.getUsername(),user.getPassword()));
				//	authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		//final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final UserDetails  userDetails =  userService.loadUserByUsername(user.getUsername());//authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
 
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		User savedUser = userService.saveUser(user);
		return ResponseEntity.ok(savedUser);
	}
	@GetMapping("/displayUserByName/{username}")
	public ResponseEntity<?> getUser(@PathVariable String username){
		
		return ResponseEntity.ok(userService.loadUserByUsername(username));
	}
	
	@GetMapping("/displayUserById/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id){
		
		return ResponseEntity.ok(userService.getUserById(id));
	}
	
	@GetMapping("/allUsers")
	public ResponseEntity<List<User>> displayAllUsers(){
		
		List<User> allUsers =  userService.getAllUsers();
		
		return ResponseEntity.ok(allUsers);
		
		
	}
	
	@PutMapping("/updateUserById/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody UserDTO user){
		
		return ResponseEntity.ok(userService.updateUser(id,user));//userDetailsService.updateUser(id, user));
	}
	
	@DeleteMapping("/deleteUserById/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id){
		
		userService.deleteUser(id);
		return new ResponseEntity<String>("Resource Deleted",HttpStatus.OK);
		
	
	}
}