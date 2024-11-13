package docker.groupmanagement.service;

import docker.groupmanagement.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    //  获取用户id信息
    User findById(int id);

    // 插入新用户
    void insert(User user);

    // 删除用户
    void delete(int id);

    //根据角色id查询用户信息
    User findByRoleId(int RoleId);

    // 获取所有用户信息
    List<User> findAllUsers();


    // 根据学号查询用户信息
    User findByStudentId(String studentId);

    // 根据姓名查询用户信息
    User findByUsername(String name);

    // 更改用户信息
    void update(User user);
}
