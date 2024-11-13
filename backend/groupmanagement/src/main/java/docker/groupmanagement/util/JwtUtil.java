package docker.groupmanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // 模拟的黑名单，实际应用可以使用 Redis 或数据库来存储
    private final Set<String> blacklistedTokens = new HashSet<>();

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

    // 检查 JWT 是否过期
    public boolean isTokenExpired(String token) {
        return verifyToken(token).getExpiration().before(new Date()) || blacklistedTokens.contains(token);
    }

    public boolean isTokenValid(String token) {
        // 检查 token 是否在黑名单中
        return  blacklistedTokens.contains(token);

    }

    // 模拟销毁 token，加入黑名单
    public void destroyToken(String token) {
        // 将 token 加入黑名单
        blacklistedTokens.add(token);
    }
}
