package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(
            @PathVariable Integer id, 
            @RequestBody Member member,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
        }

        Member existingMember = memberService.getMemberByUserId(userId);
        if (existingMember == null || !existingMember.getId().equals(id)) {
            return ResponseEntity.badRequest().body("您只能修改自己的信息");
        }

        member.setId(id);
        member.setUserId(userId);
        if (memberService.updateMember(member)) {
            return ResponseEntity.ok("更新成功");
        }
        return ResponseEntity.badRequest().body("更新失败");
    }
} 