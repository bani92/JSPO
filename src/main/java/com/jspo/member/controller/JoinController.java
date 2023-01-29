package com.jspo.member.controller;

import com.jspo.member.dao.MemberDao;
import com.jspo.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class JoinController {

    @Autowired
    private MemberDao memberDao;

    private MemberDto memberDto = MemberDto.getInstance();


    @GetMapping("/join")
    public String join(MemberDto memberDto) {

        return "regForm";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDto joinMember, Errors errors, Model m) throws Exception {

//        post 요청시 넘어온 joinMember 입력값에서 validation에 걸리는 경우
        if (errors.hasErrors()) {
//            회원가입 실패시 입력 데이터 유지
            m.addAttribute("memberDto", joinMember);
//            회원가입 실패시 message 값들을 모델에 매핑해서 view에 전달
            Map<String, String> validateMap = new HashMap<>();

            for (FieldError error : errors.getFieldErrors()) {
                System.out.println(error);
                String validKeyName = "valid_" + error.getField();
                validateMap.put(validKeyName, error.getDefaultMessage());
            }

            if (validateMap.get("valid_birth") != null) {
                validateMap.put("valid_birth", "생년월일을 입력해주세요.");
            }

//            map.keySet() -> 모든 key값을 가져온다.
//            그 가져온 키로 반복문을 통해 키와 에러 메세지로 매핑
            for (String key : validateMap.keySet()) {
//                ex) m.addAttribute("valid_email", "이메일을 입력해주세요");
                m.addAttribute(key, validateMap.get(key));
            }

            return "regForm";
        }

//        DB에 등록한 회원 저장.
        memberDao.insertMember(joinMember);
        m.addAttribute("memberDto", joinMember);
        System.out.println("join 성공");

        return "myPage";
    }

    @PostMapping("/emailCheck")
    @ResponseBody
    public int emailCheck(String email) throws Exception {
        System.out.println("email = " + email);
        if (memberDao.emailCheck(email) == null) {
            return 0;
        }
        return memberDao.emailCheck(email);
    }
}
