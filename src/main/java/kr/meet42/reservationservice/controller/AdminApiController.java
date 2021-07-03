package kr.meet42.reservationservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.meet42.reservationservice.service.AdminService;
import kr.meet42.reservationservice.service.ReservationService;
import kr.meet42.reservationservice.web.dto.AdminDecideRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationResponseDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/admin")  // TODO : 어노테이션 커스터마이징 -> @42MeetAdmin (requestMapping("admin") 이랑 Admin인지 확인하는 로직까지)
@RequiredArgsConstructor
@RestController
public class AdminApiController {
    private final ReservationService reservationService;
    private final AdminService adminService;

    @ApiOperation(value = "(Admin)승인 반려 결정", notes = "(Admin)승인 반려 결정")
    @PostMapping("/decide")
    public ResponseEntity<?> decide(@RequestBody AdminDecideRequestDto dto, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        dto.setAccessToken(accessToken);
        return adminService.decideApproveOrReject(dto);
    }

    @ApiOperation(value = "(Admin)진행, 예정, 만료, 승인대기 조회", notes = "(Admin)진행, 예정, 만료, 승인대기 조회")
    @GetMapping("")
    public ResponseEntity<List<List<ReservationResponseDto>>> getAll(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return adminService.findAllReservation(accessToken);
    }

    @ApiOperation(value = "(Admin)진행 중인 예약 조회", notes = "(Admin)진행 중인 예약 조회")
    @GetMapping("/progress")
    public ResponseEntity<List<ReservationResponseDto>> getProgress(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return adminService.findReservationByStatus(accessToken, 1L);
    }

    @ApiOperation(value = "(Admin)예정된 예약 조회", notes = "(Admin)예정된 예약 조회")
    @GetMapping("/scheduled")
    public ResponseEntity<List<ReservationResponseDto>> getScheduled(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return adminService.findReservationByStatus(accessToken, 2L);
    }

    @ApiOperation(value = "(Admin)만료된 예약 조회", notes = "(Admin)만료된 예약 조회")
    @GetMapping("/expired")
    public ResponseEntity<List<ReservationResponseDto>> getExpired(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return adminService.findReservationByStatus(accessToken, 0L);
    }

    @ApiOperation(value = "(Admin)승인 대기 조회", notes = "(Admin)승인 대기 조회")
    @GetMapping("/waiting")
    public ResponseEntity<List<ReservationResponseDto>> getWaiting(HttpServletRequest request, HttpServletResponse response) {
        // 운영진만 조회 가능
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return adminService.findReservationByStatus(accessToken, 3L);
    }
}
