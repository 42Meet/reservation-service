package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.client.MemberServiceClient;
import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.entity.Room;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.domain.repository.RoomRepository;
import kr.meet42.reservationservice.utils.JWTUtil;
import kr.meet42.reservationservice.web.dto.AdminDecideRequestDto;
import kr.meet42.reservationservice.web.dto.AdminListUpRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationDeleteRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.transaction.Transactional;
import java.util.*;

@EnableSwagger2
@RequiredArgsConstructor
@Service
public class AdminService {
    private final RoomRepository roomRepository;
    private final String gaepo = "개포";
    private final String seocho = "서초";
    private final JWTUtil jwtUtil;
    private final MemberServiceClient memberServiceClient;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @Transactional
    public List<AdminListUpRequestDto> findAllRooms() {
        List<AdminListUpRequestDto> dtos = new ArrayList<AdminListUpRequestDto>(2);
        List<Room> rooms = roomRepository.findByLocation(gaepo);
        dtos.add(
                AdminListUpRequestDto.builder()
                        .location(gaepo)
                        .roomName(roomLists(rooms))
                        .build()
        );
        rooms = roomRepository.findByLocation(seocho);
        dtos.add(
                AdminListUpRequestDto.builder()
                        .location(seocho)
                        .roomName(roomLists(rooms))
                        .build()
        );
        return dtos;
    }

    public List<String> roomLists(List<Room> rooms) {
        List<String> roomNames = new ArrayList<String>();
        for (Iterator<Room> iter = rooms.iterator(); iter.hasNext(); ){
            roomNames.add(iter.next().getRoomName());
        }
        return roomNames;
    }

    public void saveRoomList() {
        save("개포","경복궁");
        save("개포","덕수궁");
        save("개포","창경궁");
        save("개포","3층A");
        save("개포","3층B");
        save("서초","7클러스터");
        save("서초","8클러스터");
        save("서초","9클러스터");
        save("서초","10클러스터");
    }

    public void save(String location, String room) {
        roomRepository.save(Room.builder()
                .location(location)
                .roomName(room)
                .build());
    }

    public ResponseEntity<List<ReservationResponseDto>> findAllWaitings(String accessToken) {
        String intra = jwtUtil.validateAndExtract(accessToken);
        String role = memberServiceClient.getRole(intra);
        if (!role.equals("ROLE_ADMIN"))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        List<Reservation> resRaw = reservationRepository.findAllByStatusOrderByDateAsc(3L);
        List<ReservationResponseDto> res = reservationService.getReservationResponseDtos(resRaw);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> decideApproveOrReject(AdminDecideRequestDto dto) {
        String accessToken = dto.getAccessToken();
        String intra = jwtUtil.validateAndExtract(accessToken);
        String role = memberServiceClient.getRole(intra);
        // ADMIN이 아니거나 ids와 results의 개수가 같지 않으면 BAD REQUEST
        if (!role.equals("ROLE_ADMIN") || dto.getResult().isEmpty() || dto.getId().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Long id = dto.getId().get();
        Optional<Reservation> finded = reservationRepository.findById(id);
        // 유효하지 않은 예약이면
        if (finded.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // status가 3L이 아닌 것에 대해 decide를 내리려고 하면
        if (finded.get().getStatus() != 3L)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        setReservationStatusApproveOrReject(finded.get(), dto.getResult().get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @org.springframework.transaction.annotation.Transactional
    public void setReservationStatusApproveOrReject(Reservation reservation, Boolean judge) {
        // 예약승인시 Expected
        if (judge == true)
            reservation.setStatus(2L);
        // 거절시 Rejected
        else
            reservation.setStatus(4L);
    }
}
