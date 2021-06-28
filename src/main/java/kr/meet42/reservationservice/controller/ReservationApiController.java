package kr.meet42.reservationservice.controller;

import io.swagger.annotations.ApiOperation;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.service.AdminService;
import kr.meet42.reservationservice.service.ReservationService;
import kr.meet42.reservationservice.web.dto.AdminListUpRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationResponseDto;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationDeleteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
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

    @ApiOperation(value = "예약 생성", notes = "예약 생성")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ReservationSaveRequestDto requestDto, HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return reservationService.save(requestDto, accessToken);
    }

    @ApiOperation(value = "예약 현황 조회", notes = "예약 현황 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ReservationResponseDto>> findAll(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return new ResponseEntity<>(reservationService.findAllReservationByParam(paramMap), HttpStatus.OK);
    }

    @ApiOperation(value = "예약 삭제", notes = "예약 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ReservationDeleteRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        requestDto.setAccessToken(accessToken);
        return reservationService.delete(requestDto);
    }

    @ApiOperation(value = "마이페이지", notes = "마이페이지")
    @GetMapping("/mypage")
    public ResponseEntity<List<List<ReservationResponseDto>>> myReservation(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return reservationService.findMyReservation(request, accessToken);
    }

    @ApiOperation(value = "회의실 명단 조회", notes = "회의실 명단 조회")
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
