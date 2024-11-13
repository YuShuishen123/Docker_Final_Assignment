package docker.groupmanagement.service.imp;


import docker.groupmanagement.entity.User;
import docker.groupmanagement.mapper.UserMapper;
import docker.groupmanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public final UserMapper userMapper;
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    // 通过用户ID获取用户信息
    @Override
    public User findById(int id) {
        return userMapper.findById(id);
    }
    // 插入用户
    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }
    // 删除用户
    @Override
    public void delete(int id) {
        userMapper.delete(id);
    }

    @Override
    public User findByRoleId(int RoleId) {
        return userMapper.findByRoleId(RoleId);
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }

    @Override
    public User findByStudentId(String studentId) {
        return userMapper.findByStudentId(studentId);
    }

    @Override
    public User findByUsername(String name) {
        return userMapper.findByUsername(name);
    }

    public void update(User user) {
        userMapper.update(user);
    }
}
