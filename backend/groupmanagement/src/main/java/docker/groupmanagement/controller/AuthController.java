package docker.groupmanagement.controller;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.service.RoleService;
import docker.groupmanagement.util.JwtUtil;
import docker.groupmanagement.util.PasswordEncryption;
import docker.groupmanagement.util.Result;
import docker.groupmanagement.util.TokenCheck;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // 引入RoleService
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final PasswordEncryption passwordEncryption;
    private final TokenCheck tokenCheck;

    public AuthController(RoleService roleService,PasswordEncryption passwordEncryption, JwtUtil jwtUtil,TokenCheck tokenCheck) {
        this.roleService = roleService;
        this.passwordEncryption = passwordEncryption;
        this.jwtUtil = jwtUtil;
        this.tokenCheck = tokenCheck;
    }
    // 注册功能
    @RequestMapping("/register")
    public Result register(@RequestParam String username, String password, String roleName) {
        Result result = new Result();
        // 校验输入
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            result.setCode(400);
            result.setMsg("用户名或密码不能为空");
            return result;
        }
        // 现在密码是明文，后面再做加密处理
        // 现在数据库查询该username是否被占用
        try {
            if (roleService.findByUsername(username) != null) {
                result.setCode(400);
                result.setMsg("用户名已被占用");
            } else {
                // 对密码进行哈希加密
                String password_hash = passwordEncryption.passwordEncryption(password);
                // 插入新用户
                Role role = new Role(username, password_hash, roleName);
                roleService.insert(role);
                result.setCode(200);
                result.setMsg("注册成功");
            }
            return result;
        } catch (DataAccessException e) {
            result.setCode(500);
            result.setMsg("数据库错误");
            System.out.println("数据库操作发生异常：" + e.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("服务器发生异常：" + e.getMessage());
            return result;
        }
    }
    // 登陆验证功能
    @RequestMapping("/login")
    public Result login(@RequestParam String username, String password) {
        Result result = new Result();
        // 输入格式验证
        try {
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                result.setCode(400);
                result.setMsg("用户名或密码不能为空");
                return result;
            }
            Role role = roleService.findByUsername(username);
            System.out.println(role);
            if(role == null){
                result.setCode(400);
                result.setMsg("用户不存在");
                return result;
            }
            // 验证密码是否正确
            if (passwordEncryption.verifyPassword(password, role.getPassword_hash())) {
                result.setCode(200);
                result.setMsg("登陆成功");
                result.setToken(jwtUtil.generateToken(role.getUsername()));
            } else {
                result.setCode(400);
                result.setMsg("密码错误");
            }
            return result;
        } catch (DataAccessException e) {
            result.setCode(500);
            result.setMsg("数据库错误");
            System.out.println("数据库操作发生异常：" + e.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("服务器发生异常：" + e.getMessage());
            return result;
        }
    }

    // 修改密码功能
    @RequestMapping("/changePassword")
    public Result changePassword(@RequestParam String newPassword, HttpServletRequest request) {
        Result result = new Result();
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        // 判断token是否为空
        if (token == null || token.isEmpty()) {
            result.setCode(400);
            result.setMsg("token为空");
            return result;
        }
        // 移除前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 使用TokenCheck类进行token验证
        if(null != tokenCheck.checkToken(token)){
            return tokenCheck.checkToken(token);
        }
        try {
            // 从token中提取用户名
            Claims claims = jwtUtil.verifyToken(token);
            String username = claims.getSubject();

            // 根据用户名找到角色
            Role role = roleService.findByUsername(username);
            System.out.println("用户名：" + username);

            if (role == null) {
                result.setCode(400);
                result.setMsg("用户不存在");
                return result;
            }

            // 修改密码
            String newPasswordHash = passwordEncryption.passwordEncryption(newPassword);  // 或者使用 BCryptPasswordEncoder
            roleService.updatePassword(role.getId(), newPasswordHash);
            System.out.println("新密码哈希：" + newPasswordHash);

            // 销毁token
            result.setCode(200);
            result.setMsg("密码修改成功");
            jwtUtil.destroyToken(token);
            return result;
        } catch (DataAccessException e) {
            result.setCode(500);
            result.setMsg("数据库错误");
            System.out.println("数据库操作发生异常：" + e.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("服务器发生异常：" + e.getMessage());
            return result;
        }
    }

    // 退出登录
    @RequestMapping("/logout")
    public Result logout(HttpServletRequest request) {
        Result result = new Result();
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        // 判断token是否为空
        if (token == null || token.isEmpty()) {
            result.setCode(400);
            result.setMsg("token为空");
            return result;
        }
        // 移除前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 使用TokenCheck类进行token验证
        if(null != tokenCheck.checkToken(token)){
            return tokenCheck.checkToken(token);
        }
        try {
            // 销毁token
            result.setCode(200);
            result.setMsg("退出登陆");
            jwtUtil.destroyToken(token);
            return result;
        } catch (DataAccessException e) {
            result.setCode(500);
            result.setMsg("数据库错误");
            System.out.println("数据库操作发生异常：" + e.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("服务器发生异常：" + e.getMessage());
            return result;
        }
    }

    // 修改角色名称
    @RequestMapping("/changeRoleName")
    public Result changeRoleName(@RequestParam String newRoleName, HttpServletRequest request) {
        Result result = new Result();
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        // 判断token是否为空
        if (token == null || token.isEmpty()) {
            result.setCode(400);
            result.setMsg("token为空");
            return result;
        }
        // 移除前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 使用TokenCheck类进行token验证
        if(null != tokenCheck.checkToken(token)){
            return tokenCheck.checkToken(token);
        }
        try{
            // 从token中提取用户名
            Claims claims = jwtUtil.verifyToken(token);
            String username = claims.getSubject();

            // 根据用户名找到角色
            Role role = roleService.findByUsername(username);

            if (role == null) {
                result.setCode(400);
                result.setMsg("用户不存在");
                return result;
            }
            // 修改role_name
            roleService.updateRoleName(role.getId(), newRoleName);
            System.out.println("新角色名：" + newRoleName);

            result.setCode(200);
            result.setMsg("角色名称修改成功");
            return result;
        }catch (DataAccessException e) {
            result.setCode(500);
            result.setMsg("数据库错误");
            System.out.println("数据库操作发生异常：" + e.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("服务器发生异常：" + e.getMessage());
            return result;
        }
    }

    // 注销角色功能
    @RequestMapping("/deleteRole")
    public Result deleteRole(HttpServletRequest request) {
        Result result = new Result();
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        // 判断token是否为空
        if (token == null || token.isEmpty()) {
            result.setCode(400);
            result.setMsg("token为空");
            return result;
        }
        // 移除前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 使用TokenCheck类进行token验证
        if(null != tokenCheck.checkToken(token)){
            return tokenCheck.checkToken(token);
        }
        try{
            // 从token中提取用户名
            Claims claims = jwtUtil.verifyToken(token);
            String username = claims.getSubject();

            // 根据用户名找到角色
            Role role = roleService.findByUsername(username);
            System.out.println("用户名：" + username);

            // 删除该角色
            int deleteResult = roleService.delete(role.getId());
            if (deleteResult == 0) {
                result.setCode(400);
                result.setMsg("删除失败");
                return result;
            }
            result.setCode(200);
            result.setMsg("删除成功");
            jwtUtil.destroyToken(token);
            return result;

        }catch (DataAccessException e) {
            result.setCode(500);
            result.setMsg("数据库错误");
            System.out.println("数据库操作发生异常：" + e.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("服务器发生异常：" + e.getMessage());
            return result;
        }


    }
}


