package com.example.fitchallenge.Security.JWT;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 🔑 Lấy giá trị từ application.yml
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${jwt.refreshExpiration:604800000}") // Default 7 days
    private long refreshExpiration;

    // ✅ Tạo token
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)   // 🔥 THÊM ROLE VÀO TOKEN
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    // ✅ Lấy username từ token
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    // ✅ Tạo refresh token
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);
        
        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }
    
    // ✅ Kiểm tra token hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("❌ Unsupported token");
        } catch (MalformedJwtException e) {
            System.out.println("❌ Invalid token");
        } catch (SignatureException e) {
            System.out.println("❌ Invalid signature");
        } catch (Exception e) {
            System.out.println("❌ Token invalid");
        }
        return false;
    }
}
