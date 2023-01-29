package com.jspo.member.dao;

import com.jspo.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {

    // 회원가입
    void insertMember(MemberDto memberDto) throws Exception;

    // 회원조회
    MemberDto selectMember(MemberDto memberDto) throws Exception;


    MemberDto login(MemberDto memberDto) throws Exception;

    // 이메일 중복확인

    Integer emailCheck(String inputEmail) throws Exception;

    MemberDto selectMemberByEmail (String email) throws Exception;

    Integer memberPhoneCount(String phone) throws Exception;

}
