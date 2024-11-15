# 需求文档

- 实现完整的用户认证和角色管理功能

- 登陆后跳转到全部成员信息界面，渲染宣布成员的信息，以卡片的形式保存到网页上

- 角色可以创建用户，用户的外键绑定角色的ID

- 角色只可以修改自己的用户信息（根据request获取角色id，获取对应的用户id，进而修改用户信息）


## 项目简介 🎯

这是一个基于 Spring Boot 的用户认证和角色管理系统，提供了完整的用户认证和角色管理功能。

## 安全特性 🔒

1. 密码加密：使用加密算法对密码进行哈希处理
2. JWT 认证：使用 JWT 进行用户身份验证
3. Token 验证：所有需要认证的接口都会验证 token 的有效性

## 注意事项 📢

1. 所有需要认证的接口必须在请求头中携带 token
2. token 格式：`Bearer {token_string}`
3. 密码修改后需要重新登录
4. 注销账号后，相关 token 将立即失效



# 接口文档

## 基础信息

- **服务器地址**: `http://localhost:8080`

- **接口基础路径**:

  - 角色相关：`http://localhost:8080/roles`
  - 用户相关：`http://localhost:8080/users`

- **Content-Type**: `application/json`

- **字符编码**: `UTF-8`

  

## 数据库设计

### roles表（角色表）

| 字段名        | 类型         | 约束                        | 说明         |
| ------------- | ------------ | --------------------------- | ------------ |
| id            | INT          | PRIMARY KEY, AUTO_INCREMENT | 角色ID       |
| username      | VARCHAR(100) | NOT NULL, UNIQUE            | 用户登录名   |
| password_hash | VARCHAR(255) | NOT NULL                    | 加密后的密码 |
| role_name     | VARCHAR(50)  | NOT NULL                    | 角色名称     |

### users表（用户表）

| 字段名     | 类型         | 约束                          | 说明         |
| ---------- | ------------ | ----------------------------- | ------------ |
| id         | INT          | PRIMARY KEY, AUTO_INCREMENT   | 用户ID       |
| student_id | VARCHAR(20)  | NOT NULL, UNIQUE              | 学号         |
| name       | VARCHAR(100) | NOT NULL                      | 姓名         |
| work       | VARCHAR(255) | NOT NULL                      | 负责工作     |
| class_     | VARCHAR(100) | NOT NULL                      | 班级         |
| role_id    | INT          | NOT NULL, UNIQUE, FOREIGN KEY | 关联的角色ID |

注意：

1. role_id 是外键，关联到 roles 表的 id 字段
2. 当删除角色时，会级联删除对应的用户记录
3. role_id 设置为 UNIQUE，确保一个角色只能创建一个用户

# 接口文档 📚

## API 接口说明 📋

- 200: 操作成功
- 400: 请求参数错误或业务逻辑错误
- 500: 服务器内部错误

1. 响应格式：
   json
   {
   "msg": "响应信息",
   "data": "响应数据（可选）",
   "token": "JWT令牌（可选）"
   }
2. 特别说明：
   - `data`为null时不会返回此字段
   - `token`为null时不会返回此字段
   - 只有登录接口成功时会返回token
   - 错误响应通常只包含msg字段

## 全局异常处理

1. 自定义业务异常 (400 Bad Request)

   ```json
   {
       "msg": "具体的错误信息"
   }
   ```

2. 参数缺失异常 (400 Bad Request)

   ```json
   {
       "msg": "请求中缺少必要的参数"
   }
   ```

3. 参数非法异常 (400 Bad Request)

   ```json
   {
       "msg": "非法参数"
   }
   ```

4. 数据库异常 (500 Internal Server Error)

   ```json
   {
       "msg": "数据库操作异常，请稍后重试"
   }
   ```

5. 其他未知异常 (500 Internal Server Error)

   ```json
   {
       "msg": "服务器发生错误，请稍后再试"
   }
   ```

## 角色认证相关接口

### 1. 注册

- **接口URL**: `/roles/register`

- **请求方式**: POST

- **参数传递方式**: Query Parameters

- **请求参数**:

  | 参数名   | 类型   | 必填 | 说明     |
  | -------- | ------ | ---- | -------- |
  | username | String | 是   | 用户名   |
  | password | String | 是   | 密码     |
  | roleName | String | 是   | 角色名称 |

- **成功响应**:

  ```json
  {
      "msg": "注册成功"
  }
  ```

- **失败响应**:

  ```json
  {
      "msg": "用户名已被占用"
  }
  ```

  ```json
  {
      "msg": "用户名或密码不能为空"
  }
  ```

### 2. 登录

- **接口URL**: `/roles/login`

- **请求方式**: POST

- **参数传递方式**: Query Parameters

- **请求参数**:

  | 参数名   | 类型   | 必填 | 说明   |
  | -------- | ------ | ---- | ------ |
  | username | String | 是   | 用户名 |
  | password | String | 是   | 密码   |

- **成功响应**:

  ```json
  {
      "msg": "登录成功",
      "data": [用户列表],
      "token": "jwt令牌字符串"
  }
  ```

- **失败响应**:

  ```json
  {
      "msg": "用户名不存在"
  }
  ```

  ```json
  {
      "msg": "密码错误"
  }
  ```

  ```json
  {
      "msg": "用户名或密码不能为空"
  }
  ```

[接下来的部分保持相同格式，我继续写...]

### 3. 修改密码

- **接口URL**: `/roles/changePassword`

- **请求方式**: PATCH

- **参数传递方式**: Query Parameters + Headers

- **请求参数**:

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | newPassword   | String | 是   | 新密码                |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "密码修改成功"
  }
  ```

注意：修改密码成功后会使当前token失效，需要重新登录

### 4. 退出登录

- **接口URL**: `/roles/logout`

- **请求方式**: POST

- **参数传递方式**: Headers

- **请求参数**:

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "退出登录"
  }
  ```

### 5. 修改角色名称

- **接口URL**: `/roles/changeRoleName`

- **请求方式**: PATCH

- **参数传递方式**: Query Parameters + Headers

- **请求参数**:

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | newRoleName   | String | 是   | 新角色名称            |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "角色名称修改成功"
  }
  ```

### 6. 注销角色

- **接口URL**: `/roles/deleteRole`

- **请求方式**: DELETE

- **参数传递方式**: Headers

- **请求参数**:

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "注销成功"
  }
  ```

## 用户管理相关接口

### 1. 创建用户

- **接口URL**: `/users/add`

- **请求方式**: POST

- **参数传递方式**: Query Parameters + Headers

- **请求参数**:

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | student_Id    | String | 是   | 学号                  |
  | name          | String | 是   | 姓名                  |
  | work          | String | 是   | 工作                  |
  | class_        | String | 是   | 班级                  |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "创建成功",
      "data": {
          "id": 1,
          "student_Id": "学号",
          "name": "姓名",
          "work": "工作",
          "class_": "班级",
          "roleId": 1
      }
  }
  ```

- **失败响应**:

  ```json
  {
      "msg": "该角色已创建用户，请勿重复创建"
  }
  ```

### 2. 根据ID查询用户

- **接口URL**: `/users/getUserById/{id}`

- **请求方式**: GET

- **参数传递方式**: Path Variable

- **请求参数**:

  | 参数名 | 类型    | 必填 | 说明                 |
  | ------ | ------- | ---- | -------------------- |
  | id     | Integer | 是   | 用户ID (URL路径参数) |

- **成功响应**:

  ```json
  {
      "msg": "查询成功",
      "data": {
          "id": 1,
          "student_Id": "学号",
          "name": "姓名",
          "work": "工作",
          "class_": "班级",
          "roleId": 1
      }
  }
  ```

### 3. 获取所有用户

- **接口URL**: `/users/getAllUser`

- **请求方式**: GET

- **参数传递方式**: 无

- **成功响应**:

  ```json
  {
      "msg": "查询成功",
      "data": [
          {
              "id": 1,
              "student_Id": "学号1",
              "name": "姓名1",
              "work": "工作1",
              "class_": "班级1",
              "roleId": 1
          },
          // ... 更多用户
      ]
  }
  ```

- **无数据响应**:

  ```json
  {
      "msg": "暂无用户"
  }
  ```

### 4. 更新用户信息

- **接口URL**: `/users/update`

- **请求方式**: PUT

- **参数传递方式**: Request Body + Headers

- **请求参数**:

  ```json
  {
      "id": 1,
      "student_Id": "新学号",
      "name": "新姓名",
      "work": "新工作",
      "class_": "新班级"
  }
  ```

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "更新成功",
      "data": {
          "id": 1,
          "student_Id": "新学号",
          "name": "新姓名",
          "work": "新工作",
          "class_": "新班级",
          "roleId": 1
      }
  }
  ```

### 5. 删除用户

- **接口URL**: `/users/delete`

- **请求方式**: DELETE

- **参数传递方式**: Headers

- **请求参数**:

  | 参数名        | 类型   | 必填 | 说明                  |
  | ------------- | ------ | ---- | --------------------- |
  | Authorization | String | 是   | Bearer Token (请求头) |

- **成功响应**:

  ```json
  {
      "msg": "删除成功"
  }
  ```

## 注意事项

1. 除了注册和登录接口外，所有接口都需要在请求头中携带token
2. token格式为：`Bearer {token字符串}`
3. token相关：
   - 登录成功时获取token
   - 修改密码后token失效
   - 注销账号后token失效
   - 退出登录后token失效
4. 数据规则：
   - 每个角色只能创建一个用户信息
   - 用户的student_Id必须唯一
   - 角色的username必须唯一
5. JsonInclude说明：
   - Result类中的data和token字段使用了@JsonInclude(JsonInclude.Include.NON_NULL)
   - User类使用了@JsonInclude(JsonInclude.Include.NON_DEFAULT)
   - 意味着这些字段为null或默认值时不会出现在响应JSON中

