package com.example.FIT_Challenge.Security.JWT;


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

    // ✅ Tạo token
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)            // Người dùng
                .setIssuedAt(now)                // Ngày phát hành
                .setExpiration(expiryDate)       // Ngày hết hạn
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))  // Ký token bằng khóa bí mật
                .compact();
    }

    // ✅ Lấy username từ token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
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
