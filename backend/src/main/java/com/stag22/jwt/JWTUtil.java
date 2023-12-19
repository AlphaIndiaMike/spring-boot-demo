package com.stag22.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service
public class JWTUtil {
	@Value("#{'${jwtutil.issuer}'}")
	private String issuer;
	
	@Value("#{'${jwtutil.validity_days}'}")
	private Integer validity_days;
	
	private static final String SECRET_KEY = "pass_123456789_pass_123456789_pass_123456789_pass_123456789";
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	public String issueToken(String subject) {
		return issueToken(subject, Map.of());
	}
	
	public String issueToken(String subject, String ...scopes) {
		return issueToken(subject, Map.of("scopes", scopes));
	}
	
	public String issueToken(String subject, List<String> scopes) {
		return issueToken(subject, Map.of("scopes", scopes));
	}
	
	public String issueToken(String subject, Map<String, Object> claims) {
		String token = Jwts
		.builder()
		.setClaims(claims)
		.setSubject(subject)
		.setIssuer(this.issuer)
		.setIssuedAt(Date.from(Instant.now()))
		.setExpiration(
				Date.from(
						Instant.now().plus(this.validity_days, ChronoUnit.DAYS)
						)
				)
		.signWith(getSigningKey(), SignatureAlgorithm.HS256)
		.compact();
		return token;
	}
	
	public String getSubject(String token) {
		if (token == null) return "";
		Claims claims = getClaims(token);
		return claims.getSubject();
	}
	
	public Claims getClaims(String token) {
	    System.out.println("JWT String: " + token);

	    // Check if the token is null or empty
	    if (token == null || token.isEmpty()) {
	        System.out.println("Token is null or empty");
	        return null;
	    }

	    try {
	        // Parse the token
	        Claims claims = Jwts.parserBuilder()
	                            .setSigningKey(getSigningKey())
	                            .build()
	                            .parseClaimsJws(token)
	                            .getBody();
	        return claims;
	    } catch (JwtException e) {
	        // Log the exception - this covers malformed and other JWT related exceptions
	        System.err.println("Error parsing JWT: " + e.getMessage());
	        return null;
	    }
	}

	public boolean isTokenValid(String jwt, String username) {
		String subject = getSubject(jwt);
		return subject.equals(username) && !isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		Date now = Date.from(Instant.now());
		Claims claims = getClaims(jwt);
		if (claims == null) return true;
		return claims.getExpiration().before(now);
	}
}
