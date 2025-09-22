package com.project.blogs.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. get Token
		String requestToken = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;

		// Bearer 234555gkre
		System.out.println(requestToken);

		if (requestToken != null && requestToken.startsWith("Bearer")) {

			jwtToken = requestToken.substring(7);

			try {
				username = this.jwtTokenHelper.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token " + e);
			} catch (ExpiredJwtException e) {
				System.out.println("JWT token has expired " + e);
			} catch (MalformedJwtException e) {
				System.out.println("Invalid JWT " + e);
			}
		} else {
			System.out.println("JWT token does not begin with Bearer");
		}

		// once we get token , now validate it
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
			if (this.jwtTokenHelper.validateToken(jwtToken, userDetails)) {

				// all set, need authentication
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				System.out.println("Invalid JWT Token");
			}

		} else {
			System.out.println("Username is null or context is not null");
		}

		filterChain.doFilter(request, response);
	}

}
