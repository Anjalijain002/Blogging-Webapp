package com.project.blogs.security;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenHelper implements Serializable {

	
	private static final long serialVersionUID = -11L;
	
	public static final long JWT_TOKEN_VALLIDITY = 5 * 60 * 60;
	
	
//	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	private String secret = "thisIsLongKeyWhichIsEnoughForJwtTokenthisIsLongKeyWhichIsEnoughForJwtTokenthisIsLongKeyWhichIsEnoughForJwtToken";
	
	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	//retrieve expiration data from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//for retriveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secret).build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	//check if token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//generate token from user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims =  new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	
	//while creating a token
	//1. Define claims of token, like Issuer, Expiration, Subjct, and the Id
	//2. Sign the JWT using HS512 algorithm and secret key.
	//3. According to JWT concept serialization(http://tools.ietf.org/html/fraft-ietf-jose
	// compaction of the JWT to a URL-safe string
	@SuppressWarnings("deprecation")
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALLIDITY*1000))
				.signWith( SignatureAlgorithm.HS512, secret).compact();
	}
	
	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
}
