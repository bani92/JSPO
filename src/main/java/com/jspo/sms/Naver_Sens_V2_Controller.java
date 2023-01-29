package com.jspo.sms;

import com.jspo.member.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Naver_Sens_V2_Controller {

    @Autowired
    private Naver_Sens_V2_Service naver_sens_v2_service;

    @Autowired
    private MemberDao memberDao;

    static String code;

    @PostMapping("/phoneAuth")
    @ResponseBody
    public Boolean phoneAuth(String phone) throws Exception {

        System.out.println("phone = " + phone);

//        try { // 이미 가입된 전화번호가 있으면
//            if(memberDao.memberPhoneCount(phone) > 0)
//                System.out.println("memberDao.memberPhoneCount(phone) = " + memberDao.memberPhoneCount(phone));
//                return false;
//        } catch (Exception e) {
//            System.out.println("catch error");
//            e.printStackTrace();
//        }

        if (memberDao.memberPhoneCount(phone) != 0) {
            System.out.println("이미 등록된 핸드폰 번호입니다.");
            return false;
        }

        System.out.println("등록 가능한 핸드폰 번호입니다.");

        code = naver_sens_v2_service.sendRandomMessage(phone);

        return true;
    }

    @PostMapping("/phoneAuthOk")
    @ResponseBody
    public Boolean phoneAuthOk(String inputNum) throws Exception {
        System.out.println("inputNum = " + inputNum);

        if (code.equals(inputNum)) {
            return true;
        }

        return false;
    }
}
