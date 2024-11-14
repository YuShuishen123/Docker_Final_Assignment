package docker.groupmanagement.controller;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.service.RoleService;
import docker.groupmanagement.service.UserService;
import docker.groupmanagement.util.JwtUtil;
import docker.groupmanagement.util.PasswordEncryption;
import docker.groupmanagement.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class AuthController {
    // 引入RoleService
    private final RoleService roleService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncryption passwordEncryption;

    public AuthController(RoleService roleService,PasswordEncryption passwordEncryption, JwtUtil jwtUtil,UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncryption = passwordEncryption;
        this.jwtUtil = jwtUtil;
    }
    // 注册功能
    @PostMapping("/register")
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
    @PostMapping("/login")
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
                result.setData(userService.findAllUsers());
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
    @PatchMapping("/changePassword")
    public Result changePassword(@RequestParam String newPassword, HttpServletRequest request) {
        Result result = new Result();
        try {
            // 从请求中获取用户名（之前过滤器中已经设置了）
            String username = (String) request.getAttribute("username");
            String token = (String) request.getAttribute("token");
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
            int rowsAffected = roleService.updatePassword(role.getId(), newPasswordHash);
            if (rowsAffected == 0) {
                result.setCode(400);
                result.setMsg("密码修改失败");
                return result;
            }
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
        // 从请求中获取加工后的 token（在过滤器中已存储）
        String token = (String) request.getAttribute("token");
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
    @PatchMapping("/changeRoleName")
    public Result changeRoleName(@RequestParam String newRoleName, HttpServletRequest request) {
        Result result = new Result();
        try{
            // 从请求中获取用户名（之前过滤器中已经设置了）
            String username = (String) request.getAttribute("username");
            // 根据用户名找到角色
            Role role = roleService.findByUsername(username);
            if (role == null) {
                result.setCode(400);
                result.setMsg("用户不存在");
                return result;
            }
            // 修改role_name
            int updateResult = roleService.updateRoleName(role.getId(), newRoleName);
            if(updateResult == 0){
                result.setCode(400);
                result.setMsg("角色名称修改失败");
            }else{
                result.setCode(200);
                result.setMsg("角色名称修改成功");
            }
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
    @DeleteMapping("/deleteRole")
    public Result deleteRole(HttpServletRequest request) {
        Result result = new Result();
        try{
            // 从请求中获取加工后的 token（在过滤器中已存储）
            String token = (String) request.getAttribute("token");
            // 从请求中获取用户名（之前过滤器中已经设置了）
            String username = (String) request.getAttribute("username");
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


