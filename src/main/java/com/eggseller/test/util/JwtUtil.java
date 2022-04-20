package com.eggseller.test.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {
	private final PasswordEncoder passwordEncoder;
	
	private static final String SECRET_KEY = "jwtsecretkey";
	public static final long TOKEN_PERIOD = 24 * 60 * 60 * 1000;

	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		return claimsResolver.apply(claims);
	}

	public String getId(String token) {
		return getClaim(token, Claims::getId);
	}
	
	public String getSubject(String token) {
		return getClaim(token, Claims::getSubject);
	}
	
	public Date getIssuedDate(String token) {
		return getClaim(token, Claims::getIssuedAt);
	}
	
	public Date getExpirationDate(String token) {
		return getClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDate(token);
		return expiration.before(new Date());
	}

	public String generateToken(String id, String username) {
		Map<String, Object> claims = new HashMap<>();
		Long timeMillis = System.currentTimeMillis();
		//new Date((((System.currentTimeMillis()/1000/60/60+9)/24))24-9)60601000);
		
		return Jwts.builder()
				.setClaims(claims)
				.setId(id)
				.setSubject(username)
				.setIssuedAt(new Date(timeMillis))
				.setExpiration(new Date(timeMillis + TOKEN_PERIOD))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getSubject(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	public Boolean validateToken(String token, String jwtId) {
		return null != token && null != jwtId && getId(token).equals(jwtId) && !isTokenExpired(token);
	}
}
