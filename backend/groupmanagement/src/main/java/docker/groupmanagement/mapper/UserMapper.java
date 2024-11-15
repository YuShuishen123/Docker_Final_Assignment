package docker.groupmanagement.mapper;

import docker.groupmanagement.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 根据 role_id 查询用户信息
    @Select("SELECT id, student_id, name, work, class_ FROM users WHERE role_id = #{roleId}")
    User findByRoleId(int roleId);

    // 插入新用户
    @Insert("INSERT INTO users (student_id, name, work, class_, role_id) VALUES ( #{student_Id}, #{name}, #{work}, #{class_}, #{roleId})")
    int  insert(User user);

    // 删除用户
    @Delete("DELETE FROM users WHERE id = #{id}")
    int delete(int id);

    // 根据 id 查询用户信息
    @Select("SELECT id, name, student_id, work, class_, role_id FROM users WHERE id = #{id}")
    User findById(int id);

    // 根据学号查询信息
    @Select("SELECT id, name, student_id, work, class_ FROM users WHERE student_id = #{student_Id}")
    User findByStudentId(String studentId);

    @Select("SELECT id, name, student_id, work, class_ FROM users WHERE name = #{name}")
    User findByUsername(String name);

    // 更改用户信息
    @Update("UPDATE users SET name = #{user.name}, student_id = #{user.student_Id}, work = #{user.work}, class_ = #{user.class_} WHERE role_id = #{role_id}")
    int update(@Param("user") User user, @Param("role_id") int roleId);

    //获取所有用户信息
    @Select("SELECT id, name, student_id, work, class_ FROM users")
    List<User> findAllUsers();

    // 根据角色ID删除用户
    @Delete("DELETE FROM users WHERE role_id = #{roleId}")
    int deleteByRoleId(int roleId);
}
