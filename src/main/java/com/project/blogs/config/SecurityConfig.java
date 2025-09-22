package com.project.blogs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.project.blogs.security.CustomUserDetailService;
import com.project.blogs.security.JwtAuthenticationEntryPoint;
import com.project.blogs.security.JwtAuthenticationFilter;

import jakarta.servlet.Filter;

@SuppressWarnings("deprecation")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class SecurityConfig {
	
	
	public static final String[] PUBLIC_URLS= {
			"/api/v1/auth/**", 
			"/v3/api-docs",
			"/v2/api-docs",
			"/swagger-resources/**",
			"/swagger-ui/**",
			"/webjars/**"
	};
	

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean 
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf().disable()
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(PUBLIC_URLS).permitAll()
	            .requestMatchers(HttpMethod.GET).permitAll()
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}


	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	} 
	
	  @Bean public AuthenticationManager
	  authenticationManager(AuthenticationConfiguration config) throws Exception {
	  return config.getAuthenticationManager(); }
	 

}