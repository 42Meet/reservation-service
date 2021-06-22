package kr.meet42.reservationservice.controller;

import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.service.AdminService;
import kr.meet42.reservationservice.service.ReservationService;
import kr.meet42.reservationservice.web.dto.AdminListUpRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationDeleteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ReservationApiController {

    private final ReservationService reservationService;
    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ReservationSaveRequestDto requestDto) {
        return reservationService.save(requestDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Reservation>> findAll(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return new ResponseEntity<>(reservationService.findAllReservationByParam(paramMap), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ReservationDeleteRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader("jwt");
        requestDto.setJwt(jwt);
        return reservationService.delete(requestDto);
    }

    @GetMapping("/mypage")
    public List<List<Reservation>> myReservation(HttpServletRequest request, HttpServletResponse response) {
        return reservationService.findMyReservation(request);
    }

    @GetMapping("/rooms")
    public List<AdminListUpRequestDto> listUp(HttpServletRequest request, HttpServletResponse response) {
//        adminService.saveRoomList();
        return adminService.findAllRooms();
    }

    @GetMapping("/init")
    public void init() {
        adminService.saveRoomList();
    }
}
