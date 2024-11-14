package docker.groupmanagement.service.imp;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.entity.User;
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
        return roleMapper.updatePassword(id, passwordHash);
    }

    // 新增角色
    @Override
    public int insert(Role role) {
        return roleMapper.insert(role);
    }
    // 注销角色
    @Override
    public int delete(int id) {
        return roleMapper.delete(id);

    }
    // 根据用户名查询角色信息
    @Override
    public Role findByUsername(String username) {
        return roleMapper.findByUsername(username);
    }
    // 修改角色名称（非用户名），返回受影响行数
    @Override
    public int updateRoleName(int id, String roleName) {
        return roleMapper.updateRoleName(id, roleName);
    }

    // 根据role_id查询绑定所有user信息
    @Override
    public List<User> findUsersByRoleId(int roleId) {
        return roleMapper.findUsersByRoleId(roleId);
    }

}
