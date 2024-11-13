package docker.groupmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA标记id字段自增
    private int id;
    private String username;
    private String password_hash;
    private String role_name;

    public Role(String username, String passwordHash, String roleName) {
        this.username = username;
        this.password_hash = passwordHash;
        this.role_name = roleName;
    }

    public Role() {
    }
}
