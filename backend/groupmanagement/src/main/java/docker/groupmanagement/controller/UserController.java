package docker.groupmanagement.controller;


import docker.groupmanagement.service.UserService;
import docker.groupmanagement.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
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
        try{
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(userService.findAllUsers());
            return result;
        }catch(Exception e){
            result.setCode(500);
            result.setMsg("服务器错误");
            System.out.println("获取所有用户信息时发生异常");
            return result;
        }
    }

}
