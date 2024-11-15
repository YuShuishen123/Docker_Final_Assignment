package docker.groupmanagement.service.imp;


import docker.groupmanagement.entity.User;
import docker.groupmanagement.exception.CustomException;
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
        User user = userMapper.findById(id);
        if(user == null){
            throw new CustomException("用户不存在", "USER_NOT_FOUND");
        }
        return user;
    }


    // 插入用户
    @Override
    public int insert(User user) {
        int insertResult =  userMapper.insert(user);
        if(insertResult == 0){
            throw new CustomException("用户添加失败", "USER_INSERT_FAILED");
        }
        return insertResult;
    }
    // 删除用户
    @Override
    public int delete(int id) {
        int deleteResult = userMapper.delete(id);
        if (deleteResult == 0){
            throw new CustomException("用户删除失败", "USER_DELETE_FAILED");
        }
        return deleteResult;
    }

    @Override
    public User findByRoleId(int RoleId) {
        if(userMapper.findByRoleId(RoleId) == null){
            throw new CustomException("用户不存在", "USER_NOT_FOUND");
        }
        return userMapper.findByRoleId(RoleId);
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }

    @Override
    public User findByStudentId(String studentId) {
        if(userMapper.findByStudentId(studentId) == null){
            throw new CustomException("用户不存在", "USER_NOT_FOUND");
        }
        return userMapper.findByStudentId(studentId);
    }

    @Override
    public User findByUsername(String name) {
        if(userMapper.findByUsername(name) == null){
            throw new CustomException("用户不存在", "USER_NOT_FOUND");
        }
        return userMapper.findByUsername(name);
    }

    public int update(User user, int role_id) {
        int updateResult = userMapper.update(user, role_id);
        if (updateResult == 0){
            throw new CustomException("用户更新失败", "USER_UPDATE_FAILED");
        }
        return updateResult;
    }

    // 根据角色ID删除用户
    @Override
    public int deleteByRoleId(int roleId) {
        int deleteResult = userMapper.deleteByRoleId(roleId);
        if (deleteResult == 0){
            throw new CustomException("删除失败","USER_DELETE_FAILED");
        }
        return deleteResult;
    }
}
