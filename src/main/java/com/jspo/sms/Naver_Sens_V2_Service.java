package com.jspo.sms;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Naver_Sens_V2_Service {

    public Naver_Sens_V2_Service() {}

    public String sendRandomMessage(String phone) {
        Naver_Sens_V2 message = new Naver_Sens_V2();
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        System.out.println("회원가입 문자 인증 => " + numStr);

        message.send_msg(phone, numStr);

        return numStr;
    }
}
