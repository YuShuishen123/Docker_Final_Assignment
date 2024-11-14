package docker.groupmanagement.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // 默认值为 0 的字段不输出
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String student_Id;
    private String name;
    private String work;
    private String class_;
    private int roleId;

    // Constructors, getters, and setters
    public User() {
    }
    public User(String studentId, String name, String work, String class_, int roleId) {
        this.student_Id = studentId;
        this.name = name;
        this.work = work;
        this.class_ = class_;
        this.roleId = roleId;
    }
}