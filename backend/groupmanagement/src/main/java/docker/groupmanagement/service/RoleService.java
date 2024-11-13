package docker.groupmanagement.service;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.entity.User;
import org.springframework.stereotype.Service;


@Service
public interface RoleService {
    // 修改密码
    int updatePassword(int id, String passwordHash);

    // 新增角色
    int insert(Role role);

    // 注销角色
    int delete(int id);

    // 根据role_id查询user信息
    User findByRoleId(int roleId);

    // 根据用户名查询角色信息
    Role findByUsername(String username);

    // 修改角色名
    int updateRoleName(int id, String roleName);

}
