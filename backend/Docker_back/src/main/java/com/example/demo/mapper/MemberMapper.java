package com.example.demo.mapper;

import com.example.demo.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM member")
    List<Member> findAll();

    @Select("SELECT * FROM member WHERE user_id = #{userId}")
    Member findByUserId(Integer userId);

    @Update("UPDATE member SET name = #{name}, student_number = #{studentNumber} " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int updateMember(Member member);
} 