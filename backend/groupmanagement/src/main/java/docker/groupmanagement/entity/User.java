package docker.groupmanagement.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class User {
    @Id
    private int id;
    private String student_Id;
    private String name;
    private String work;
    private String class_;
    private int role_Id;
    // Getters and setters
    public User(String username, String studentId, String name, String work, String className, int roleId) {
    }

    public User() {

    }
}