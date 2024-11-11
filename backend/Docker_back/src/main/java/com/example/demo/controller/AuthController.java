package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Member;
import com.example.demo.service.UserService;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
  @Autowired
  private UserService userService;

  @Autowired
  private MemberService memberService;

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestParam String username,
      @RequestParam String password,
      HttpSession session) {
    User user = userService.login(username, password);
    if (user != null) {
      Member member = memberService.getMemberByUserId(user.getId());
      session.setAttribute("userId", user.getId());
      session.setAttribute("username", user.getUsername());

      Map<String, Object> response = new HashMap<>();
      response.put("message", "登录成功");
      response.put("userId", user.getId());
      response.put("member", member);
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.badRequest().body("用户名或密码错误");
  }
}