package docker.groupmanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    // 注入共享黑名单
    TokenBlacklist tokenBlacklist;
    public JwtUtil(TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // 生成 JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 验证和解析 JWT
    public Claims verifyToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查 JWT 是否有效
    public boolean isTokenExpired(String token) {
        return verifyToken(token).getExpiration().before(new Date()) || tokenBlacklist.isTokenBlacklisted(token);
    }

    // 模拟销毁 token，加入黑名单
    public void destroyToken(String token) {
        // 将 token 加入黑名单
        tokenBlacklist.addToBlacklist(token);
    }
}
