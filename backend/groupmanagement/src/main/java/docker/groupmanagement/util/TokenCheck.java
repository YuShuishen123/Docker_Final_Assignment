package docker.groupmanagement.util;

import org.springframework.stereotype.Component;

@Component
public class TokenCheck {
    private final JwtUtil jwtUtil;

    public TokenCheck(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Result checkToken(String token) {
        Result result = new Result();  // 每次调用都创建一个新的 Result 实例

        // 验证token是否无效
        if (jwtUtil.isTokenExpired(token)) {
            result.setMsg("token无效");
            return result;
        }

        // 返回 null，表示 token 有效
        return null;  // token有效
    }
}
