package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;

    public List<Member> getAllMembers() {
        return memberMapper.findAll();
    }

    public Member getMemberByUserId(Integer userId) {
        return memberMapper.findByUserId(userId);
    }

    public boolean updateMember(Member member) {
        return memberMapper.updateMember(member) > 0;
    }
} 