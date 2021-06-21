package kr.meet42.reservationservice.controller;

import kr.meet42.reservationservice.service.ReservationService;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class ReservationApiController {
    private final ReservationService reservationService;
    @PostMapping("/new")
    public void register(@RequestBody ReservationSaveRequestDto requestDto) {
//        System.out.println("requestDto = " + requestDto.getMember());
        //        System.out.println("requestDto.getMember().get(0) = " + requestDto.getMember().get(0));
        ArrayList<String> members = requestDto.getMembers();
        // Long leader_id;
        boolean result;
        result = reservationService.save(requestDto);
        System.out.println("result = " + result);
    }
//
//    @GetMapping("/v1/reservation/new")
//    public Long save(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("request = " + request);
//        //reservationService.save(requestDto);
//        return 1L;
//    }
}
