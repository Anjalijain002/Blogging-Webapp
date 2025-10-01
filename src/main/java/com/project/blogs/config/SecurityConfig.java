package com.project.blogs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.project.blogs.security.CustomUserDetailService;
import com.project.blogs.security.JwtAuthenticationEntryPoint;
import com.project.blogs.security.JwtAuthenticationFilter;


@SuppressWarnings("deprecation")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc

public class SecurityConfig {
	

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
	            .cors().and()  
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
	                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                .anyRequest().authenticated()
	            )
	            .exceptionHandling()
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            .and()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }


	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	} 
	
	  @Bean 
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	  return config.getAuthenticationManager(); 
	  
	  }
	  
	  
	  @Bean
	  public FilterRegistrationBean coresFilter() {
		  
		  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		  
		  CorsConfiguration corsConfiguration = new CorsConfiguration();
		  corsConfiguration.setAllowCredentials(true);
		  corsConfiguration.addAllowedOriginPattern("*");
		  corsConfiguration.addAllowedHeader("Authorization");
		  corsConfiguration.addAllowedHeader("Content-Type");
		  corsConfiguration.addAllowedHeader("Accept");
		  corsConfiguration.addAllowedMethod("POST");
		  corsConfiguration.addAllowedMethod("GET");
		  corsConfiguration.addAllowedMethod("DELETE");
		  corsConfiguration.addAllowedMethod("PUT");
		  corsConfiguration.addAllowedMethod("OPTIONS");
		  corsConfiguration.setMaxAge(3600L);
		  

		  source.registerCorsConfiguration("/**", corsConfiguration);
	
		  FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		 
		  return bean;
	  }
}