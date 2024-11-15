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
        // 检测该角色是否已创建用户
        if(roleService.findUsersByRoleId(roleId) != null){
            result.setMsg("该角色已创建用户，请勿重复创建");
            return result;
        }
        User user = new User(student_Id, name, work, class_, roleId);
        userService.insert(user);
        result.setMsg("创建成功");
        result.setData(user);
        return result;
}

    // 根据用户id获取用户信息
    @GetMapping("/getUserById/{id}")
    public Result queryUserbyId(@PathVariable int id){
        Result result = new Result();
        userService.findById(id);
        result.setMsg("查询成功");
        result.setData(userService.findById(id));
        return result;
}

    // 获取所有用户信息
    @GetMapping("/getAllUser")
    public Result queryAllUser(){
        Result result = new Result();
        if(userService.findAllUsers().isEmpty()){
            result.setMsg("暂无用户");
            return result;
        }
        result.setData(userService.findAllUsers());
        result.setMsg("查询成功");
        return result;
    }

    // 更新用户信息
    @PutMapping("/update")
    public Result update(@RequestBody User user,HttpServletRequest request){
        Result result = new Result();
        int role_id = (int) request.getAttribute("roleId");
        userService.update(user,role_id);
        result.setMsg("更新成功");
        result.setData(user);
        return result;
    }

    // 根据角色ID删除用户
    @DeleteMapping("/delete")
    public Result delete(HttpServletRequest request){
        Result result = new Result();
        int roleId = (int) request.getAttribute("roleId");
        userService.findByRoleId(roleId);
        userService.deleteByRoleId(roleId);
        result.setMsg("删除成功");
        return result;
    }

}
