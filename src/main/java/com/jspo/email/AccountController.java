package com.jspo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    // 인증번호 저장하는 변수.
    static String auth;

    public AccountController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private final EmailService emailService;

    // 인증번호 생성 및 저장.
    @PostMapping("join/emailConfirm")
    @ResponseBody
    public void mailConfirm(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);
        auth = code;
    }

    // 인증번호 확인시 사용자가 입력한 값과 서버에 저장된 인증번호가 일치하는지 확인하여 true false 반환.
    @PostMapping("join/emailAuth")
    @ResponseBody
    public boolean authConfirm(@RequestParam String inputNum) throws Exception {
        return auth.equals(inputNum);
    }
}