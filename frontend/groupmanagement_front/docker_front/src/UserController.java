package docker.groupmanagement.controller;

import docker.groupmanagement.entity.User;
import docker.groupmanagement.service.RoleService;
import docker.groupmanagement.service.UserService;
import docker.groupmanagement.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController (UserService userService,RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // 创建用户
    @PostMapping("/add")
    public Result createUser(@RequestParam String student_Id,
                             @RequestParam String name,
                             @RequestParam String work,
                             @RequestParam String class_,
                             HttpServletRequest request) { // 直接注入 HttpServletRequest
        Result result = new Result();
        int roleId =  (int) request.getAttribute("roleId");
        System.out.println("用户角色ID：" + roleId);
        // 检测该角色是否已创建用户
        if(!roleService.findUsersByRoleId(roleId).isEmpty()){
            result.setCode(400);
            result.setMsg("该角色已创建用户，请勿重复创建");
            return result;
        }
        try{
            User user = new User(student_Id, name, work, class_, roleId);
            System.out.println("创建用户：" + user);
            int UserCreate = userService.insert(user);
            if(UserCreate == 0){
                result.setCode(400);
                result.setMsg("创建失败");
                return result;
            }
            System.out.println("创建用户成功");
            result.setCode(200);
            result.setMsg("创建成功");
            result.setData(user);
            return result;
        }catch(Exception e){
            result.setCode(500);
            result.setMsg("服务器错误");
            return result;
        }
    }

    // 根据用户id获取用户信息
    @GetMapping("/getUserById/{id}")
    public Result queryUserbyId(@PathVariable int id){
        Result result = new Result();
        try {
            if(userService.findById(id) == null){
                result.setCode(300);
                result.setMsg("用户不存在");
            }else{
                result.setCode(200);
                result.setMsg("查询成功");
                result.setData(userService.findById(id));
            }
            return result;
        }catch(Exception e){
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("根据ID查询时发生异常");
            return result;
        }
    }

    // 获取所有用户信息
    @GetMapping("/getAllUser")
    public Result queryAllUser(){
        Result result = new Result();
        result.setData(userService.findAllUsers());
        try{
            result.setCode(200);
            result.setMsg("查询成功");
            return result;
        }catch(Exception e){
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("获取所有用户信息时发生异常");
            return result;
        }
    }

    // 更新用户信息
    @PutMapping("/update")
    public Result update(@RequestBody User user,HttpServletRequest request){
        Result result = new Result();
        int role_id = (int) request.getAttribute("roleId");
        System.out.println("用户角色ID：" + role_id);
        System.out.println("更新用户信息：" + user + "角色ID：" + role_id);
        try{
            int update = userService.update(user,role_id);
            System.out.println("更新用户信息：" + user + "角色ID：" + role_id);
            if(update == 0){
                result.setCode(400);
                result.setMsg("更新失败");
                return result;
            }
            result.setCode(200);
            result.setMsg("更新成功");
            result.setData(user);
            return result;
        }catch(Exception e){
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("更新用户信息时发生异常");
            return result;
        }
    }

    // 根据角色ID删除用户
    @DeleteMapping("/delete")
    public Result delete(HttpServletRequest request){
        Result result = new Result();
        int roleId = (int) request.getAttribute("roleId");
        System.out.println("用户角色ID：" + roleId);
        try{
            if(userService.findByRoleId(roleId) == null){
                result.setCode(400);
                result.setMsg("你还未创建用户");
                return result;
            }
            int deleteResult = userService.deleteByRoleId(roleId);
            if(deleteResult == 0){
                System.out.println(userService.findByRoleId(roleId));
                result.setCode(400);
                result.setMsg("删除失败");
                System.out.println("删除失败");
                return result;
            }
            result.setCode(200);
            result.setMsg("删除成功");
            return result;
        }catch(Exception e){
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("删除用户时发生异常");
            return result;
        }
    }

}
