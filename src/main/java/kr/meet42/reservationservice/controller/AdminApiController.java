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

// @RequestMapping("/admin")  TODO : 어노테이션 커스터마이징 -> @42MeetAdmin (requestMapping("admin") 이랑 Admin인지 확인하는 로직까지)
@RequiredArgsConstructor
@RestController
public class AdminApiController {
    private final ReservationService reservationService;
    private final AdminService adminService;

    @ApiOperation(value = "승인 대기 조회", notes = "승인 대기 조회")
    @GetMapping("/waitings")
    public ResponseEntity<List<ReservationResponseDto>> getWaitings(HttpServletRequest request, HttpServletResponse response) {
        // 운영진만 조회 가능
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return adminService.findAllWaitings(accessToken);
    }

    @ApiOperation(value = "승인 반려 결정", notes = "승인 반려 결정")
    @PostMapping("/decide")
    public ResponseEntity<?> decide(@RequestBody AdminDecideRequestDto dto, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access-token");
        if (accessToken == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        dto.setAccessToken(accessToken);
        return adminService.decideApproveOrReject(dto);
    }
}
