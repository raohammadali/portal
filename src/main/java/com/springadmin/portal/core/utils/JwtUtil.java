package com.springadmin.portal.core.utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.springadmin.portal.core.exceptions.InvalidJWTTokenException;
import com.springadmin.portal.core.model.Permission;
import com.springadmin.portal.core.model.Role;
import com.springadmin.portal.core.model.User;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    @Value("${JWT_SECRET:9VlqvY6RA4GJqqoDtV7RKhypGogOVB+v3BnpTYWOMglgFo3TWNOaJ5W9Zfw0YOeckqCdRoezuXTk+hqCLiZrHg==}")
    private String secretKey;

    @Value("${JWT_A_EXPIRATION:86400000}")
    private long expirationTime;

    @Value("${jwt.issuer:skynet-auth-service}")
    private String issuer;
    private volatile Key signingKey;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private Key getSigningKey() {
        if (signingKey == null) {
            synchronized (this) {
                if (signingKey == null) {
                    if (secretKey.length() < 64) {
                        log.warn("JWT secret key should be at least 64 characters for HS512 algorithm");
                    }
                    signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
                }
            }
        }
        return signingKey;
    }
    public String generateToken(User user, String adminEmail) {
        return generateToken(user, adminEmail, expirationTime);
    }

    public String generateToken(User user, String adminEmail, long customExpiration) {
        Objects.requireNonNull(user, "User cannot be null");
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(customExpiration);

        Set<String> permissions = extractPermissions(user);
        Set<String> roles = extractRoles(user);

        JwtBuilder tokenBuilder = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("permissions", permissions)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512);

        if (adminEmail != null && !adminEmail.trim().isEmpty()) {
            tokenBuilder.claim("impersonatedBy", adminEmail.trim());
            tokenBuilder.claim("isImpersonated", true);
        }

        String token = tokenBuilder.compact();
        log.debug("Generated JWT token for user: {}", user.getEmail());
        return token;
    }

    private Set<String> extractPermissions(User user) {
        return user.getPermissions().stream()
                .map(Permission::getName)
                .filter(Objects::nonNull)
                .filter(permission -> !permission.trim().isEmpty())
                .collect(Collectors.toUnmodifiableSet());
    }

    private Set<String> extractRoles(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .filter(Objects::nonNull)
                .filter(role -> !role.trim().isEmpty())
                .collect(Collectors.toUnmodifiableSet());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractClaim(token, "userId", Long.class);
    }

    public boolean validateToken(String token) {
        if (isTokenBlacklisted(token)) {
            log.warn("Attempt to use blacklisted token");
            return false;
        }

        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("Malformed JWT token: {}", ex.getMessage());
        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error during token validation: {}", ex.getMessage());
        }
        return false;
    }

    public <T> T extractClaim(String token, String claimName, Class<T> type) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName, type);
    }

    public Set<String> extractPermissions(String token) {
        List<String> permissions = extractClaim(token, "permissions", List.class);
        return permissions != null ? 
            Set.copyOf(permissions) : Collections.emptySet();
    }

    public Set<String> extractRoles(String token) {
        List<String> roles = extractClaim(token, "roles", List.class);
        return roles != null ? 
            Set.copyOf(roles) : Collections.emptySet();
    }

    public boolean isImpersonated(String token) {
        Boolean isImpersonated = extractClaim(token, "isImpersonated", Boolean.class);
        return Boolean.TRUE.equals(isImpersonated);
    }

    public Optional<String> getImpersonator(String token) {
        return Optional.ofNullable(extractClaim(token, "impersonatedBy", String.class));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token expired: {}", ex.getMessage());
            throw new InvalidJWTTokenException("Token has expired", ex);
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token: {}", ex.getMessage());
            throw new InvalidJWTTokenException("Unsupported token format", ex);
        } catch (MalformedJwtException ex) {
            log.warn("Malformed JWT token: {}", ex.getMessage());
            throw new InvalidJWTTokenException("Malformed token", ex);
        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature: {}", ex.getMessage());
            throw new InvalidJWTTokenException("Invalid token signature", ex);
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty: {}", ex.getMessage());
            throw new InvalidJWTTokenException("Token claims are empty", ex);
        } catch (Exception ex) {
            log.error("Unexpected error parsing JWT token: {}", ex.getMessage());
            throw new InvalidJWTTokenException("Invalid token", ex);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();

        Set<String> roles = extractRoles(token);
        Set<String> permissions = extractPermissions(token);

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.addAll(roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()));

        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()));

        log.debug("Created authentication for user: {} with {} authorities", 
                 username, authorities.size());
        
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public void blacklistToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            blacklistedTokens.add(token.trim());
            log.debug("Token blacklisted");
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public Date getExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean willExpireWithin(String token, long milliseconds) {
        Date expiration = getExpirationDate(token);
        Date now = new Date();
        return expiration.getTime() - now.getTime() <= milliseconds;
    }

    public String refreshToken(String oldToken) {
        Claims claims = extractAllClaims(oldToken);
        blacklistToken(oldToken);

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}