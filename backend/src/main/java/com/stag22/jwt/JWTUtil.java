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
		return getClaims(token).getSubject();
	}
	
	public Claims getClaims(String token) {
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims;
	}

	public boolean isTokenValid(String jwt, String username) {
		String subject = getSubject(jwt);
		
		return subject.equals(username) && !isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		Date now = Date.from(Instant.now());
		return getClaims(jwt).getExpiration().before(now);
	}
	
}
