package com.jspo.reservation.controller;

import com.jspo.hotel.dao.HotelDao;
import com.jspo.hotel.dto.HotelDto;
import com.jspo.member.dao.MemberDao;
import com.jspo.member.dto.MemberDto;
import com.jspo.reservation.dao.ReservationDao;
import com.jspo.reservation.dao.ReservedDao;
import com.jspo.reservation.dto.ReservationDto;
import com.jspo.room.dao.RoomDao;
import com.jspo.room.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReservationController {

    @Autowired
    private HotelDao hotelDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservedDao reservedDao;

    private HotelDto hotelDto = HotelDto.getInstance();
    private RoomDto roomDto = RoomDto.getInstance();
    private MemberDto memberDto = MemberDto.getInstance();
    private ReservationDto reservationDto = ReservationDto.getInstance();

    @PostMapping("/reservation")
    public String reserve(HttpSession session, Model m, int hotelHtId, int rId,
                          java.sql.Date rCheckin, java.sql.Date rCheckout, HttpServletRequest request) throws Exception {

        System.out.println("post reservation = " + request.getHeader("referer"));
        
        if (session.getAttribute("email") == null) {
            return "redirect:/login";
        }

        // 현재 예약중인 memberDto, hotelDto, roomDto 객체 DB에서 읽어와서 reservation 페이지로 넘겨줌
        hotelDto = hotelDao.selectHotelByHtId(hotelHtId);
        memberDto = memberDao.selectMemberByEmail((String) session.getAttribute("email"));
        roomDto = roomDao.selectRoomByRId(rId);
        roomDto.setrCheckin(rCheckin);
        roomDto.setrCheckout(rCheckout);
        // reservationDto 객체에 예약정보 입력
        reservationDto.setResPrice(roomDto.getrPrice());
        reservationDto.setMemberMId(memberDto.getId());

        long inTime = rCheckin.getTime();
        long outTime = rCheckout.getTime();
        long diff = (outTime - inTime)/1000/60/60/24;
        reservationDto.setRoomRCheckin(new Date(inTime+(1000*60*60*24)));
        reservationDto.setRoomRCheckout(new Date(outTime+(1000*60*60*24)));

        reservationDto.setRoomHotelHtId(hotelHtId);
        reservationDto.setRoomRId(rId);

        Date now = new Date();
        reservationDto.setResDate(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String res = simpleDateFormat.format(now)+hotelHtId+rId+memberDto.getId();
        reservationDto.setResId(res);
        // reservationDto 끝

        m.addAttribute(hotelDto);
        m.addAttribute(roomDto);
        m.addAttribute(memberDto);
        m.addAttribute("diff", diff);
        m.addAttribute(reservationDto);

        return "reservation";
    }

    @PostMapping("/reservation/complete")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public void complete(@RequestBody Map<String, String> data) {
        // imp_uid, merchant_uid, amount
        // 결제 성공 reservation DB에 정보 입력.

        long inTime = reservationDto.getRoomRCheckin().getTime();
        long outTime = reservationDto.getRoomRCheckout().getTime();
        long diff = (outTime - inTime)/1000/60/60/24;

        Date date = new Date();

        Map<String, Object> map = new HashMap<>();

        for (long i = inTime; i <= outTime; i += 86400000) {
            date.setTime(i);
            map.put("roomHotelHtId", reservationDto.getRoomHotelHtId());
            map.put("roomRId", reservationDto.getRoomRId());
            map.put("roomRCheckin", date);
            reservedDao.insertReserved(map);
        }

        reservationDao.insertReservation(reservationDto);
    }

    @PostMapping("/reservation/cancel/{resId}")
    @CrossOrigin
    @ResponseBody
    public boolean cancel(@RequestBody Map<String, String> data, @PathVariable String resId) {

        if (data.isEmpty()) return false;

        String merchant_uid = data.get("merchant_uid");
        String resPrice = data.get("cancel_request.amount");
        String reason = data.get("reason");
        String memberName = data.get("refund_holder");

        System.out.println("주문번호 : " + merchant_uid);
        System.out.println("예약금액 : " + resPrice);
        System.out.println("취소사유 : " + reason);
        System.out.println("회원명  : " + memberName);
        System.out.println("주문번호 일치여부 : " + resId.equals(merchant_uid));
        System.out.println("예약 삭제처리 : " + reservationDao.deleteReservationByResId(resId));
        reservedDao.deleteReserved(reservationDao.selectReservationByResId(Long.parseLong(merchant_uid)));

        return true;
    }

    @GetMapping("/my/reserved")
    public String reserved(HttpSession session, Model m) throws Exception {
        if (session.getAttribute("email") == null) {

            return "login";
        }

        String email = (String) session.getAttribute("email");
        memberDto = memberDao.selectMemberByEmail(email);

        try {

            List<ReservationDto> reservation = reservationDao.selectAllReservationById(memberDto.getId());
            List<HotelDto> hotelDto = new ArrayList<>();
            List<RoomDto> roomDto = new ArrayList<>();

            for (ReservationDto reservationDto : reservation) {
                hotelDto.add(hotelDao.selectHotelByHtId(reservationDto.getRoomHotelHtId()));
                roomDto.add(roomDao.selectRoomByRId(reservationDto.getRoomRId()));
            }
            m.addAttribute("reservation", reservation);
            m.addAttribute(memberDto);
            m.addAttribute("hotelDto", hotelDto);
            m.addAttribute("roomDto", roomDto);

        } catch (Exception e) {

        }

        return "reserved";
    }
}
