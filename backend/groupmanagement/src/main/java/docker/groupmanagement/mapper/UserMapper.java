package docker.groupmanagement.mapper;

import docker.groupmanagement.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    // 根据 role_id 查询用户信息
    @Select("SELECT id, student_id, name, work, class_ FROM users WHERE role_id = #{roleId}")
    User findByRoleId(int roleId);

    // 插入新用户
    @Insert("INSERT INTO users (student_id, name, work, class_, role_id) VALUES ( #{studentId}, #{name}, #{work}, #{class}, #{roleId})")
    void insert(User user);

    // 删除用户
    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(int id);

    // 根据 id 查询用户信息
    @Select("SELECT id, name, student_id, work, class_, role_id FROM users WHERE id = #{id}")
    User findById(int id);

    // 根据学号查询信息
    @Select("SELECT id, name, student_id, work, class_ FROM users WHERE student_id = #{studentId}")
    User findByStudentId(String studentId);

    @Select("SELECT id, name, student_id, work, class_ FROM users WHERE name = #{name}")
    User findByUsername(String name);

    // 更改用户信息
    @Insert("UPDATE users SET name = #{name}, student_id = #{studentId}, work = #{work}, class_ = #{class} WHERE id = #{id}")
    void update(User user);

    //获取所有用户信息
    @Select("SELECT id, name, student_id, work, class_ FROM users")
    List<User> findAllUsers();
}
