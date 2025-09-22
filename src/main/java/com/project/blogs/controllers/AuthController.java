package com.project.blogs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.blogs.exceptions.ApiException;
import com.project.blogs.payloads.JwtAuthRequest;
import com.project.blogs.payloads.JwtAuthResponse;
import com.project.blogs.payloads.UserDto;
import com.project.blogs.security.JwtTokenHelper;
import com.project.blogs.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {


	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(
			@RequestBody JwtAuthRequest request
			) throws Exception{
		
		this.authenticate(request.getUsername(),request.getPassword());
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		
		
		JwtAuthResponse response = new JwtAuthResponse();
			response.setToken(token);	
			return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}


	private void authenticate(String username, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken
		= new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			
			this.authenticationManager.authenticate(authenticationToken);
			
		} catch (BadCredentialsException e) {
			System.out.println("Invalid Details !!");
			throw new ApiException("Invalid Username or Password !!");
		}
		
		
	}
	
	//register new user Api
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
	
		UserDto regiseteredUser = this.userService.regiseterNewUser(userDto);
		
		return new ResponseEntity<UserDto>(regiseteredUser,HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
	
	
	
}
