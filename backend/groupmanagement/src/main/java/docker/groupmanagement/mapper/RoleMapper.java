package docker.groupmanagement.mapper;

import docker.groupmanagement.entity.Role;
import docker.groupmanagement.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RoleMapper {

    // 根据用户名查询角色信息
    @Select("SELECT id, username, password_hash, role_name FROM roles WHERE username = #{username}")
    Role findByUsername(@Param("username") String username);

    // 修改密码
    @Update("UPDATE roles SET password_hash = #{passwordHash} WHERE id = #{id}")
    int updatePassword(@Param("id") int id, @Param("passwordHash") String passwordHash);

    // 新增角色
    @Insert("INSERT INTO roles (username, password_hash, role_name) VALUES (#{username}, #{password_hash}, #{role_name})")
    int insert(Role role);


    // 修改角色名称（非用户名），返回受影响行数
    @Update("UPDATE roles SET role_name = #{roleName} WHERE id = #{id}")
    int updateRoleName(@Param("id") int id, @Param("roleName") String roleName);

    // 删除角色，返回受影响行数
    @Delete("DELETE FROM roles WHERE id = #{id}")
    int delete(@Param("id") int id);

    // 根据role_id查询user信息
    @Select("SELECT student_id, name, work, class_ FROM users WHERE role_id = #{roleId}")
    User findByRoleId(int roleId);
}
