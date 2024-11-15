package docker.groupmanagement.service.imp;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.entity.User;
import docker.groupmanagement.exception.CustomException;
import docker.groupmanagement.mapper.RoleMapper;
import docker.groupmanagement.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    // 构造器注入
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    // 修改角色密码
    @Override
    public int updatePassword(int id, String passwordHash) {
        int rowsAffected = roleMapper.updatePassword(id, passwordHash);
        if (rowsAffected == 0) {
            throw new CustomException("密码修改失败", "PASSWORD_UPDATE_FAILED");
        }
        return rowsAffected;
    }

    // 新增角色
    @Override
    public int insert(Role role) {
        int rowsAffected = roleMapper.insert(role);
        if (rowsAffected == 0) {
            throw new CustomException("角色添加失败", "ROLE_INSERT_FAILED");
        }
        return rowsAffected;
    }

    // 注销角色
    @Override
    public int delete(int id) {
        int rowsAffected = roleMapper.delete(id);
        if (rowsAffected == 0) {
            throw new CustomException("角色删除失败", "ROLE_DELETE_FAILED");
        }
        return rowsAffected;
    }

    // 根据用户名查询角色信息
    @Override
    public Role findByUsername(String username) {
        return roleMapper.findByUsername(username);
    }

    // 修改角色名称（非用户名），返回受影响行数
    @Override
    public int updateRoleName(int id, String roleName) {
        int rowsAffected = roleMapper.updateRoleName(id, roleName);
        if (rowsAffected == 0) {
            throw new CustomException("角色名称更新失败", "ROLE_UPDATE_FAILED");
        }
        return rowsAffected;
    }
    // 根据role_id查询绑定的user信息
    @Override
    public User findUsersByRoleId(int roleId) {
        return roleMapper.findUsersByRoleId(roleId);
    }
}
