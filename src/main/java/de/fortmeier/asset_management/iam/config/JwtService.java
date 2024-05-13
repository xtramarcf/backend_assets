package de.fortmeier.asset_management.iam.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for JWT (JSON Web Token) operations.
 */
@Service
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  /**
   * Extracts the username from the JWT token.
   *
   * @param token JWT token
   * @return Username extracted from the token
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts a specific claim from the JWT token.
   *
   * @param token          JWT token
   * @param claimsResolver Function to resolve the claim
   * @param <T>            Type of the claim
   * @return Extracted claim
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Generates a JWT token for the provided user details.
   *
   * @param userDetails User details
   * @return Generated JWT token
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  /**
   * Generates a JWT token with additional claims for the provided user details.
   *
   * @param extraClaims   Additional claims to be included in the token
   * @param userDetails   User details
   * @return Generated JWT token
   */
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  /**
   * Builds a JWT token with specified claims and expiration time.
   *
   * @param extraClaims Additional claims to be included in the token
   * @param userDetails User details
   * @param expiration  Expiration time for the token (in milliseconds)
   * @return Generated JWT token
   */
  private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .claim("role", userDetails.getAuthorities().toArray()[0].toString())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  /**
   * Checks if a JWT token is valid for the provided user details.
   *
   * @param token       JWT token
   * @param userDetails User details
   * @return True if the token is valid, otherwise false
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  /**
   * Checks if a JWT token has expired.
   *
   * @param token JWT token
   * @return True if the token has expired, otherwise false
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date from a JWT token.
   *
   * @param token JWT token
   * @return Expiration date of the token
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts all claims from a JWT token.
   *
   * @param token JWT token
   * @return All claims from the token
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  /**
   * Retrieves the signing key used for JWT token generation and validation.
   *
   * @return Signing key
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}