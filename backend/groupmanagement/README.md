# 后端接口文档 📚

## 项目简介 🎯

这是一个基于 Spring Boot 的用户认证和角色管理系统，提供了完整的用户认证和角色管理功能。

## API 接口说明 📋

所有接口基础路径：`/auth`

### 1. 用户注册 
- 接口：`/register`
- 方法：POST
- 参数：
  - username: 用户名
  - password: 密码
  - roleName: 角色名称
- 返回：  ```json
  {
    "code": 200,
    "msg": "注册成功"
  }  ```

### 2. 用户登录
- 接口：`/login`
- 方法：POST
- 参数：
  - username: 用户名
  - password: 密码
- 返回：  ```json
  {
    "code": 200,
    "msg": "登录成功",
    "token": "jwt_token_string"
  }  ```

### 3. 修改密码
- 接口：`/changePassword`
- 方法：POST
- 请求头：
  - Authorization: Bearer {token}
- 参数：
  - newPassword: 新密码
- 返回：  ```json
  {
    "code": 200,
    "msg": "密码修改成功"
  }  ```

### 4. 退出登录
- 接口：`/logout`
- 方法：POST
- 请求头：
  - Authorization: Bearer {token}
- 返回：  ```json
  {
    "code": 200,
    "msg": "退出登录"
  }  ```

### 5. 修改角色名称
- 接口：`/changeRoleName`
- 方法：POST
- 请求头：
  - Authorization: Bearer {token}
- 参数：
  - newRoleName: 新角色名称
- 返回：  ```json
  {
    "code": 200,
    "msg": "角色名称修改成功"
  }  ```

### 6. 注销账号
- 接口：`/deleteRole`
- 方法：POST
- 请求头：
  - Authorization: Bearer {token}
- 返回：  ```json
  {
    "code": 200,
    "msg": "删除成功"
  }  ```

## 错误码说明 ⚠️

- 200: 操作成功
- 400: 请求参数错误或业务逻辑错误
- 500: 服务器内部错误

## 安全特性 🔒

1. 密码加密：使用加密算法对密码进行哈希处理
2. JWT 认证：使用 JWT 进行用户身份验证
3. Token 验证：所有需要认证的接口都会验证 token 的有效性

## 注意事项 📢

1. 所有需要认证的接口必须在请求头中携带 token
2. token 格式：`Bearer {token_string}`
3. 密码修改后需要重新登录
4. 注销账号后，相关 token 将立即失效

