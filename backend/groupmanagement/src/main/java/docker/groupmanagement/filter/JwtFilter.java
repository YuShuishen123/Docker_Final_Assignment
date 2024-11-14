package docker.groupmanagement.filter;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.service.RoleService;
import docker.groupmanagement.util.JwtUtil;
import docker.groupmanagement.util.TokenCheck;
import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenCheck tokenCheck;
    private  final RoleService roleService;


    // 构造函数注入 JwtUtil
    public JwtFilter(JwtUtil jwtUtil, TokenCheck tokenCheck, RoleService roleService) {
        this.jwtUtil = jwtUtil;
        this.tokenCheck = tokenCheck;
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        // 获取请求的URI
        String requestURI = request.getRequestURI();

        // 如果请求路径是登录或注册接口，跳过 JWT 校验
        if (requestURI.startsWith("/roles/login") || requestURI.startsWith("/roles/register")) {
            filterChain.doFilter(request, response);  // 放行，不进行 token 校验
            return;
        }


        // 获取请求头中的 Authorization
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // 获取 JWT 部分


            try {
                // 检查 token 是否有效
                // 使用TokenCheck类进行token验证
                if(null != tokenCheck.checkToken(token)){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token problem");
                    return;
                }

                // 解析并验证 JWT
                Claims claims = jwtUtil.verifyToken(token);
                // 将用户名信息设置到请求中，后续的服务可以使用
                request.setAttribute("username", claims.getSubject());
                // 将加工后的 token 存储到请求中，供后续控制器销毁
                request.setAttribute("token", token);
                // 根据用户名找到角色id
                Role role = roleService.findByUsername(claims.getSubject());
                // 将角色id设置到请求中，后续的服务可以使用
                request.setAttribute("roleId", role.getId());

            } catch (Exception e) {
                // 如果验证失败，抛出错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token problem");
                return;
            }
        } else {
            // 没有 Authorization 头，返回 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token lose");
            return;
        }

        // 继续过滤链，交给后续的处理
        filterChain.doFilter(request, response);
    }
}
