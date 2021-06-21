package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.entity.Room;
import kr.meet42.reservationservice.domain.repository.RoomRepository;
import kr.meet42.reservationservice.web.dto.AdminListUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final RoomRepository roomRepository;
    private final String gaepo = "개포";
    private final String seocho = "서초";

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
}
