package com.project.blogs.payloads;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class UserDto {

	private int id;
	
	@NotEmpty(message = "Username must be min of 4 Characters !!")
	@Size(min=4, message = "Username must be min of 4 Characters !!")
	private String name;
	
	@Email(message = "Email Address is not Valid!!")
	@NotEmpty(message = "Email is required")
	private String email;
	
	
	@NotEmpty(message = "Password must be min of 3 Chars and max of 10 Chars !!")
	@Size(min = 3, max = 10, message = "Password must be min of 3 Chars and max of 10 Chars !!")
	private String password;
	
	
	@NotEmpty
	private String about;
	
	private  Set<RoleDto> roles = new HashSet<>();
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
}
