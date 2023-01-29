package com.jspo.member.controller;

import com.jspo.member.dao.MemberDao;
import com.jspo.member.dto.MemberDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private MemberDao memberDao;

    private MemberDto memberDto = MemberDto.getInstance();

    @GetMapping("/")
    public String login() {

        return "login";
    }

    @PostMapping("/login")
    public String login(MemberDto loginMember, Model m) throws Exception {

        // 로그인 페이지에 적은 이메일과 패스워드를 memberDto에 set한다.
         memberDto.setEmail(loginMember.getEmail());
         memberDto.setPwd(loginMember.getPwd());

         // memberDto 엔 email값과 pwd 값이 있으며
        // 그 값을 sql문의 login의 조건으로 select 한다.
        MemberDto result = memberDao.login(memberDto);
        if(result != null) {
            // 결과값이 널이 아니면 즉 select가 정상적으로 되었음
            // 메인 홈으로 가기
            return "test";
        } else {
            // 로그인 실패시 로그인 페이지에 머무르며
            // 에러메시지를 띄운다
            return "redirect:/";
        }

    }
}
