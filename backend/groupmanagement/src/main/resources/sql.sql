# 创建数据库

CREATE DATABASE groupmanagement_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 创建roles表
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,  -- 存储用户的登录名
                       password_hash VARCHAR(255) NOT NULL,    -- 存储加密后的密码
                       role_name VARCHAR(50) NOT NULL          -- 存储角色名称（如 admin、member 等）
);

# 创建users表
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,     -- 用户唯一标识符
                       student_id VARCHAR(20) NOT NULL UNIQUE, -- 学号，唯一
                       name VARCHAR(100) NOT NULL,             -- 姓名
                       work VARCHAR(255) NOT NULL,             -- 负责工作
                       class VARCHAR(100) NOT NULL,            -- 班级
                       role_id INT NOT NULL,                   -- 角色 ID，外键关联 `roles` 表
                       FOREIGN KEY (role_id) REFERENCES roles(id) -- 外键关联到 `roles` 表
);


ALTER TABLE users
    ADD CONSTRAINT fk_role_id
        FOREIGN KEY (role_id)
            REFERENCES roles(id)
            ON DELETE CASCADE;


-- 插入管理员角色
INSERT INTO roles (username, password_hash, role_name)
VALUES ('admin_user', 'hashed_password_for_admin', 'admin');

-- 插入普通成员角色
INSERT INTO roles (username, password_hash, role_name)
VALUES ('member_user1', 'hashed_password_for_member1', 'member');

-- 插入更多成员角色
INSERT INTO roles (username, password_hash, role_name)
VALUES ('member_user2', 'hashed_password_for_member2', 'member');


-- 插入与管理员角色关联的用户
INSERT INTO users ( student_id, name, work, class, role_id)
VALUES ('202310001', 'Admin User', '管理工作', '计算机科学', 1);

-- 插入与普通成员角色关联的用户
INSERT INTO users ( student_id, name, work, class, role_id)
VALUES ('202310002', 'Member User 1', '开发工作', '软件工程', 2);

-- 插入另一个与普通成员角色关联的用户
INSERT INTO users ( student_id, name, work, class, role_id)
VALUES ('202310003', 'Member User 2', '测试工作', '计算机科学', 2);

delete  from roles where id=1;
