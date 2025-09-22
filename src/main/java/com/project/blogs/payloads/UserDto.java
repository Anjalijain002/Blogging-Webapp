package com.project.blogs.payloads;


import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class UserDto {

	private int id;
	
	@NotEmpty
	@Size(min=4, message = "Username must be mininum of 4 Characters!!")
	private String name;
	
	@Email(message = "Email Address is not Valid!!")
	private String email;
	
	
	@NotEmpty
	@Size(min=3, max=10, message = "Password must be minimum of 3 Character and maximum of 10 Characters!!")
	private String password;
	
	
	@NotEmpty
	private String about;
	
	private  Set<RoleDto> roles = new HashSet<>();
}
